package com.datamaster.bl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;

import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.datamaster.dao.JDBCConnection;
import com.datamaster.dao.SQLQueries;
import com.gargoylesoftware.htmlunit.BrowserVersion;

public class MSPUrlExtractor implements Job{
    String cats[];
	private void setCats(String cats[]){
		this.cats = cats;
	}
    static {
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
    }
    
    static Map<String, List<String>> urlMap;
    static boolean isRunning = false;
    
    public static void main(String ar[]) {
        
        String[] cats = new String[1];
        cats[0] = "mobiles";
        MSPUrlExtractor obj = new MSPUrlExtractor();
        obj.setCats(cats);
        
        obj.processData(); 
        
    }
    
    public void processData() {
        JDBCConnection conn1 = JDBCConnection.getInstance();
     //   MSPUrlExtractor msp = new MSPUrlExtractor();
        ExecutorService executor = Executors.newFixedThreadPool(3);
        List<Future<String>> list = new ArrayList<Future<String>>();
        urlMap = new HashMap<String, List<String>>();
        
        String fetchUrlQuery = SQLQueries.fetchAllUrl; // from msp_product_url
        fetchUrlQuery = fetchUrlQuery + " where section in (";
        for (String str : cats) {
            fetchUrlQuery = fetchUrlQuery + "'" + str + "',";
        }
        fetchUrlQuery = fetchUrlQuery.substring(0, fetchUrlQuery.lastIndexOf(","));
        fetchUrlQuery = fetchUrlQuery + ")";
        System.out.println(fetchUrlQuery);
        ResultSet rs1 = conn1.executeQuery(fetchUrlQuery, null);
        List productUrl = new ArrayList();
        if (rs1 != null) {
            try {
                while (rs1.next()) {
                    String url = rs1.getString("url");
                    
                    productUrl.add(url);
                }
                
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        String fetchQuery = SQLQueries.fetchMainCategoryMap; // from category_main_url msp
        
        fetchQuery = fetchQuery + " where section in (";
        for (String str : cats) {
            fetchQuery = fetchQuery + "'" + str + "',";
        }
        fetchQuery = fetchQuery.substring(0, fetchQuery.lastIndexOf(","));
        fetchQuery = fetchQuery + ")";
        System.out.println(fetchQuery);
        
        ResultSet rs = conn1.executeQuery(fetchQuery, null);
        Map<String, List<String>> urlMap = new HashMap<String, List<String>>();
        if (rs != null) {
            try {
                while (rs.next()) {
                    String cat = rs.getString("section");
                    
                    String first_url = rs.getString("first_page_url");
                    String second_url = rs.getString("second_page_url");
                    String number = rs.getString("total_pages");
                    urlMap.put(cat, Arrays.asList(first_url, second_url, number));
                }
                
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        urlMap.forEach((k, v) -> {
            Callable<String> callable = this.new DataExtractor(v.get(0), v.get(1), v.get(2), k, productUrl);
            Future<String> future = executor.submit(callable);
            // add Future to the list, we can get return value using Future
            list.add(future);
        });
        executor.shutdown();
        
    }
    
    class DataExtractor implements Callable {
        String baseUrl;
        String otherUrls;
        int limit;
        String productUrl;
        HtmlUnitDriver driver;
        String section;
        String keyword; // 2.html
        String query;
        List<String> params;
        List allExistingUrl;
        
        JDBCConnection conn;
        
        public DataExtractor(String baseUrl, String otherUrl, String limit, String section, List allUrl) {
            this.baseUrl = baseUrl;
            this.otherUrls = otherUrl;
            this.section = section;
            this.limit = Integer.parseInt(limit);
            driver = new HtmlUnitDriver(BrowserVersion.CHROME);
            params = new ArrayList<>();
            allExistingUrl = allUrl;
            conn = JDBCConnection.getInstance();
            // driver.setJavascriptEnabled(true);
        }
        
        @Override
        public Object call() throws Exception {
            System.out.println("Calling  for " + section);
            // get the base url data
            System.out.println(allExistingUrl.size());
            driver.get(baseUrl);
            System.out.println(baseUrl);
            for (int i = 1; i <= 51; i++) {
                try {
                    
                    if (driver.findElements(By.xpath("/html/body/div[4]/div[3]/div[1]/div[5]/div[2]/div[1]/div[" + i + "]/div[2]/a")).size() != 0)
                        productUrl = driver.findElement(By.xpath("/html/body/div[4]/div[3]/div[1]/div[5]/div[2]/div[1]/div[" + i + "]/div[2]/a")).getAttribute("href");
                    
                    else if (driver.findElements(By.xpath("/html/body/div[4]/div[3]/div[1]/div[3]/div[2]/div[1]/div[" + i + "]/div[2]/a")).size() != 0)
                        
                        productUrl = driver.findElement(By.xpath("/html/body/div[4]/div[3]/div[1]/div[3]/div[2]/div[1]/div[" + i + "]/div[2]/a")).getAttribute("href");
                    
                    else
                        productUrl = driver.findElement(By.xpath("/html/body/div[4]/div[3]/div[1]/div[4]/div[2]/div[1]/div[" + i + "]/div[2]/a")).getAttribute("href");
                    
                    if (!allExistingUrl.contains(productUrl))
                        this.saveData(productUrl, section);
                }
                catch (Exception e) {
                    System.out.println("Section " + section);
                    
                    continue;
                }
            }
            
            System.out.println("For Other pages Limit " + limit);
            // for the otherUrls
            for (int j = 2; j <= limit; j++) {
                System.out.println("Running for Page " + j);
                driver.get(otherUrls + j + ".html");
                for (int i = 1; i <= 48; i++) {
                    
                    try {
                        
                        // if(driver.findElements(By.xpath("/html/body/div[4]/div[3]/div[1]/div[4]/div[2]/div[1]/div["+i+"]/div[2]/a")).size() != 0)
                        productUrl = driver.findElement(By.xpath("/html/body/div[4]/div[3]/div[1]/div[4]/div[2]/div[1]/div[" + i + "]/div[2]/a")).getAttribute("href");
                        
                        // else //if(driver.findElements(By.xpath("/html/body/div[4]/div[3]/div[1]/div[3]/div[2]/div[1]/div["+i+"]/div[2]/a")).size() != 0)
                        // productUrl = driver.findElement(By.xpath("/html/body/div[4]/div[3]/div[1]/div[3]/div[2]/div[1]/div["+i+"]/div[2]/a")).getAttribute("href");
                        
                        if (!allExistingUrl.contains(productUrl)) {
                            this.saveData(productUrl, section);
                        }
                    }
                    catch (Exception e) {
                        System.out.println("Section " + section);
                        System.out.println(e.getMessage());
                        // e.printStackTrace();
                        continue;
                    }
                }
            }
            
            driver.close();
            return null;
        }
        
        private void saveData(String url, String section) {
            query = SQLQueries.insertMspProductUrl;
            params.add("elecaap");
            params.add(url);
            params.add(section);
            params.add("i");
            conn.upsertData(query, params);
            params.clear();
            // System.out.println(url+" "+ section);
        }
        
    }

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("Scheduling Job");
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		String cats[] =  (String[])dataMap.get("cats");
		this.setCats(cats);
		this.processData();
	}
}
