package com.datamaster.bl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;

import org.apache.commons.collections.map.HashedMap;
import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.datamaster.dao.JDBCConnection;
import com.datamaster.dao.SQLQueries;
import com.gargoylesoftware.htmlunit.BrowserVersion;

public class MSPCatDataExtractor  implements Job 	{
	 String cats[];
		private void setCats(String cats[]){
			this.cats = cats;
		}
	static {
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(
				Level.OFF);
		System.setProperty("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.NoOpLog");
	}
	JDBCConnection conn;
	Map<String, List<String>> urlMap;
	static String idBase = "ACNEW";
	int count;
	private static Map<String, Map<String, List<String>>> mainMap;
	private Map<String, List<String>> subMap;
	private List<String> urlList;

	 public void processData()  {
		
		this.getUrls();
		HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_24);
		ExecutorService executor = Executors.newFixedThreadPool(6);
		List<Future<String>> list = new ArrayList<Future<String>>();

		for(Map.Entry<String,Map<String,List<String>>> topLevelMenuMapEntry:mainMap.entrySet()){
		    for(Map.Entry<String, List<String>> sectionLevelEntry:topLevelMenuMapEntry.getValue().entrySet() ){
				Callable<String> callable = this.new DataExtractor(sectionLevelEntry.getValue(),sectionLevelEntry.getKey(), driver, idBase);
				Future<String> future = executor.submit(callable);
				list.add(future);
		    }	
		}
		// add Future to the list, we can get return value using Future
		System.out.println("Shutting down ececutor");
		//executor.shutdown();

	}

	/**
	 * Load the database urls
	 * 
	 * @author Tushar mainMap[urlMap,urlList]
	 */
	public void getUrls() {
	    System.out.println("Starting at "+new Timestamp(new Date().getTime()));
		conn = JDBCConnection.getInstance();
		// String query = SQLQueries.getMspUrls;
		String mainQuery = "select distinct menu_level1 from msp_product_url where temp_flag = 'f'";
		mainQuery = mainQuery + " and section in (";
        for (String str : getCats()) {
            mainQuery = mainQuery + "'" + str + "',";
        }
        mainQuery = mainQuery.substring(0, mainQuery.lastIndexOf(","));
        mainQuery = mainQuery + ")";
		// String query =
		// "SELECT DISTINCT section FROM msp_product_url WHERE  menu_level1 = 'mobiles' ORDER BY section;";
		ResultSet rs1 = conn.executeQuery(mainQuery, null);

		mainMap = new HashMap<>();

		try {
			while (rs1.next()) {
				String subMenuQuery = "SELECT DISTINCT section FROM msp_product_url WHERE  menu_level1 = '"
						+ rs1.getString("menu_level1") + "' and temp_flag = 'f' ORDER BY section";
				// get the prodct corr to each section
				ResultSet rs = conn.executeQuery(subMenuQuery, null);
				urlMap = new HashMap<>();
				while (rs.next()) {
					String getProductUrl = "select * from msp_product_url where section = '"
							+ rs.getString("section") + "' and temp_flag = 'f' LIMIT 100";
					ResultSet rsProductUrl = conn.executeQuery(getProductUrl,
							null);
					urlList = new ArrayList<>();
					while (rsProductUrl.next()) {
						urlList.add(rsProductUrl.getString("url"));

					}
					urlMap.put(rs.getString("section"), urlList);
				}
				mainMap.put(rs1.getString("menu_level1"), urlMap);
			}
			System.out.println("Ending  at "+new Timestamp(new Date().getTime()));
		} catch (Exception e) {
		    System.out.println("Error at "+new Timestamp(new Date().getTime()));
			e.getMessage();
		} finally {

			// conn.closeConnection();
		}
	}

	class DataExtractor implements Callable {
		String query;
		List<String> params;
		JDBCConnection conn;
		List<String> url;
		String section;
		HtmlUnitDriver driver;
		String vendorUrl;
		String deliveryTime;
		String emi;
		String cod;
		String rating;
		String image;
		String price;
		String productid;

		public DataExtractor(List<String> baseUrl, String section,HtmlUnitDriver driver, String id) {
			this.url = baseUrl;
			this.section = section;
			this.driver = driver;
			this.productid = id;
			params = new ArrayList<>();
			conn = JDBCConnection.getInstance();

		}

		@Override
		public Object call() throws Exception {

			Iterator<String> itr = url.iterator();
			String currentUrl = "";
			while (itr.hasNext()) {
				
				currentUrl = itr.next();
				
				try {
					driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_24);
					
					// get to the page
					driver.get(currentUrl);
					// image
					
					try{
						//*[@id="mspSingleImg"]
						if(driver.findElement(
								By.xpath("/html/body/div[4]/div[2]/div/div[1]/div[2]/img")).getAttribute(
										"src").length() != 0){
							image = driver.findElement(
									By.xpath("/html/body/div[4]/div[2]/div/div[1]/div[2]/img")).getAttribute(
											"src");
						}else if(driver.findElement( By.xpath("//*[@id='mspSingleImg']")).getAttribute("src").length() != 0){
							image = driver.findElement(
                                    By.xpath("//*[@id='mspSingleImg']")).getAttribute(
                                    "src");

							}
						
					}catch(Exception e){
						System.out.println("Inside Exception");
						saveSkipForNoData(currentUrl);
						continue;

					}
					for (int i = 1; i <= 11; i++) {
						if(driver.findElement(By.xpath("//*[@id='pricetable']/div[" +i+ "]/div[1]/div[6]/div[1]")).getAttribute("data-url").length() !=  0){
						vendorUrl = driver.findElement(
								By.xpath("//*[@id='pricetable']/div[" + i
										+ "]/div[1]/div[6]/div[1]"))
								.getAttribute("data-url");
						}else if( driver.findElement(
								By.xpath("//*[@id='pricetable']/div[" + i
										+ "]/div[1]/div[6]/div"))
								.getAttribute("data-url").length() !=  0){
							vendorUrl = driver.findElement(
									By.xpath("//*[@id='pricetable']/div[" + i
											+ "]/div[1]/div[6]/div"))
									.getAttribute("data-url");
						}else if(driver.findElement(
                                By.xpath("//*[@id='pricetable']/div[" + i
                                        + "]/div[2]/div[5]/div[2]/div"))
                          .getAttribute("data-url").length() !=  0){
							vendorUrl = driver.findElement(
                                    By.xpath("//*[@id='pricetable']/div[" + i
                                                  + "]/div[2]/div[5]/div[2]/div"))
                                    .getAttribute("data-url");
						}
						// delivery time
						// deliveryTime =
						// driver.findElement(By.xpath("//*[@id='pricetable']/div[3]/div[2]/div[4]/div[1]")).getText();
						//*[@id="pricetable"]/div[1]/div[1]/div[4]/div[1]
						if(driver.findElement(
								By.xpath("//*[@id='pricetable']/div[" + i
										+ "]/div[1]/div[4]/div[1]")).getText().length() !=  0){
						deliveryTime = driver.findElement(
								By.xpath("//*[@id='pricetable']/div[" + i
										+ "]/div[1]/div[4]/div[1]")).getText();}
						else if (driver.findElement(
                                By.xpath("//*[@id='pricetable']/div[" + i
                                        + "]/div[2]/div[4]/div[1]")).getText().length() !=  0) {
							deliveryTime = driver.findElement(
                                    By.xpath("//*[@id='pricetable']/div[" + i
                                            + "]/div[2]/div[4]/div[1]")).getText();
							
						}
						if(driver.findElement(
								By.xpath("//*[@id='pricetable']/div[" + i
										+ "]/div[1]/div[2]/div/div[1]/div"))
								.getAttribute("data-tooltip").length() !=  0){
							rating = driver.findElement(
								By.xpath("//*[@id='pricetable']/div[" + i
										+ "]/div[1]/div[2]/div/div[1]/div"))
								.getAttribute("data-tooltip");
						} else if ( driver.findElement(
                                By.xpath("//*[@id='pricetable']/div[" + i
                                        + "]/div[2]/div[2]/div[2]"))
                          .getAttribute("data-callout").length() !=  0){
							rating = driver.findElement(
                                    By.xpath("//*[@id='pricetable']/div[" + i
                                                  + "]/div[2]/div[2]/div[2]"))
                                    .getAttribute("data-callout");
						}
						// emi avaliable
						if(driver.findElement(
								By.xpath("//*[@id='pricetable']/div[" + i
										+ "]/div[1]/div[3]/div[2]")).getText().length() !=  0){
						emi = driver.findElement(
								By.xpath("//*[@id='pricetable']/div[" + i
										+ "]/div[1]/div[3]/div[2]")).getText();
						
						}else if (driver.findElement(
                                By.xpath("//*[@id='pricetable']/div[" + i
                                        + "]/div[2]/div[3]/div[1]")).getText().length() !=  0){
						emi =	driver.findElement(
                                    By.xpath("//*[@id='pricetable']/div[" + i
                                                  + "]/div[2]/div[3]/div[1]")).getText();
						}

						// cod
						//*[@id="pricetable"]/div[2]/div[1]/div[3]/div[1]
						if(driver.findElement(
								By.xpath("//*[@id='pricetable']/div[" + i
										+ "]/div[1]/div[3]/div[1]"))
								.getAttribute("class").length() !=  0){
						cod = driver.findElement(
								By.xpath("//*[@id='pricetable']/div[" + i
										+ "]/div[1]/div[3]/div[1]"))
								.getAttribute("class");
						}else if(driver.findElement(
                                By.xpath("//*[@id='pricetable']/div[" + i
                                        + "]/div[2]/div[3]/div[3]"))
                          .getAttribute("class").length() !=  0){
						cod =	driver.findElement(
                                    By.xpath("//*[@id='pricetable']/div[" + i
                                                  + "]/div[2]/div[3]/div[3]"))
                                    .getAttribute("class");
							
						}
						// System.out.println("URL = "+vendorUrl);
						//*[@id="pricetable"]/div[2]/div[1]/div[5]/div[1]/span[2]
						//*[@id="pricetable"]/div[2]/div[1]/div[5]/div[1]
						if(driver.findElement(
								By.xpath("//*[@id='pricetable']/div[" + i
										+ "]/div[1]/div[5]/div[1]"))
								.getText().length() !=  0){
						price = driver.findElement(
								By.xpath("//*[@id='pricetable']/div[" + i
										+ "]/div[1]/div[5]/div[1]"))
								.getText();
						}else if (driver.findElement(
                                By.xpath("//*[@id='pricetable']/div[" + i
                                        + "]/div[2]/div[5]/div[1]/div[1]"))
                          .getText().length() !=  0){
						price =	driver.findElement(
                                    By.xpath("//*[@id='pricetable']/div[" + i
                                                  + "]/div[2]/div[5]/div[1]/div[1]"))
                                    .getText();
						}
						this.saveData(currentUrl, this.productid, this.section,(currentUrl.substring(currentUrl.lastIndexOf("/") + 1,currentUrl.length())), vendorUrl,price, image, cod, deliveryTime, rating, emi);
						
					}
					

				} catch (Exception e) {
					e.getMessage();
					saveSkipForNoData(currentUrl);
					continue;
					
					
				}finally{
					driver.quit();
				}
			}

			return null;
		}

		private void saveData(String currentUrl, String... data) {
			System.out.println("Data Exists");
			query = SQLQueries.insertMspProductData;
			for(String s : data){
			    params.add(s);
			}
			
			 conn.upsertData(query, params);
			params.clear();
			query = SQLQueries.updateMSPUrlFlag;
			params.add(currentUrl);
			
			 conn.upsertData(query, params);
			params.clear();
			// System.out.println(url+" "+ section);
		}
		
		private void saveSkipForNoData(String currentUrl){
			System.out.println("No Data");
			query = SQLQueries.updateSkipForNoData; 
			params.add(currentUrl);
			 conn.upsertData(query, params);
				params.clear();
		}
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		String cats[] =  (String[])dataMap.get("cats");
		this.setCats(cats);
		this.processData();
		
	}

	public String[] getCats() {
		return cats;
	}

}
