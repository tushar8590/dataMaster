package com.datamaster.bl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.datamaster.dao.JDBCConnection;
import com.datamaster.dao.SQLQueries;


public class FillMSPProductURlColumns {

	private static String host = "jdbc:mysql://localhost:3306/aapcompare_test";
	private static String userName = "root";
	private static String password = "";


	private static Connection con;
	static JDBCConnection conn = JDBCConnection.getInstance();;


	private ResultSet rs;

	public static void main(String args[]){
		try{

			// Load the Driver class. 
			Class.forName("com.mysql.jdbc.Driver");
			// If you are using any other database then load the right driver here.

			//Create the connection using the static getConnection method
			con = DriverManager.getConnection (host,userName,password);
			con.setAutoCommit(false);

		}catch(Exception e){
			e.printStackTrace();
		}
		try {
			String query ="SELECT DISTINCT main_menu, menu_level1, menu_level2 FROM new_menu ";

			String sectionQuery ="SELECT DISTINCT section FROM msp_product_url WHERE STATUS = 'i'";

			Statement stmt = con.createStatement();
			Statement stmt1 = con.createStatement();


			ResultSet rs;

			rs = stmt.executeQuery(query);
			Map<String,String> columnMappingMap = new HashMap<>();
			while(rs.next())
			{
				try {
					String menu_level1 =rs.getString("main_menu");

					String menu_level2 =rs.getString("menu_level1");
					String section =rs.getString("menu_level2");

					if(section.equals("Choppers and Blenders")){
						section = "Choppers & Blenders";
					}else if(section.equals("Car Cradles and Mounts")){
						section ="Car Cradles & Mounts";
					}else if(section.equals("Repeaters and Extenders")){
						section ="Repeaters & Extenders";
					}else if(section.equals("Video and DVD Players")){
						section ="Video & DVD Players";
					}else if(section.equals("Processor Fans and Cooling")){
						section ="Processor Fans & Cooling";
					}else if(section.equals("Power Banks")){
						section ="Power Banks";
					}else if(section.equals("Antennas and Amplifiers")){
						section ="Antennas & Amplifiers";
					}else if(section.equals("Camera Mounts and Clamps")){
						section ="Camera Mounts & Clamps";
					}else if(section.equals("Diffusers and Modifiers")){
						section ="Diffusers & Modifiers";
					}else if(section.equals("Adapters and Converters")){
						section ="Adapters & Converters";
					}else if(section.equals("Camera and Camcorder Batteries")){
						section ="Camera & Camcorder Batteries";
					}else if(section.equals("Inverters and Batteries")){
						section ="Inverters & Batteries";
					}else if(section.equals("Laptop Chargers and Adapters")){
						section ="Laptop Chargers & Adapters";
					}else if(section.equals("Lens Cleaners and Kits")){
						section ="Lens Cleaners & Kits";
					}
					
					columnMappingMap.put(section, menu_level1+"*"+menu_level2);


				}
				catch(Exception e){
					continue;
				}

			}

			rs = stmt1.executeQuery(sectionQuery);



			while(rs.next())
			{
				String section = rs.getString("section");

				if(columnMappingMap.containsKey(section)){
					String value = columnMappingMap.get(section);
					String[] parts = value.split("\\*");
					//saveData(model,parts[0],parts[1],spec_url,section);
					List<String> params = new ArrayList<>();
					query = SQLQueries.updateMSPProductTableColumns;

					params.add(parts[0]);
					params.add(parts[1]);
					params.add(section);
					conn.upsertData(query, params);
					System.out.println("inserted");
					params.clear();
				}

			}

			System.out.println(columnMappingMap.size());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

}
