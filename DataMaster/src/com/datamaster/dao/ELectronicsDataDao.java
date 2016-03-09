package com.datamaster.dao;

import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ELectronicsDataDao {
    List<String> allCategories;
    List<String> topCategories;
    JDBCConnection conn;
    public ELectronicsDataDao(){
        conn = JDBCConnection.getInstance();
    }
    public List<String> getAllCategories(){
        return allCategories;
    }
    
    public List<String> getTopCategories(){
        return topCategories;
    }
    
    public void initiateJob(String jobName){
        List<String> params = new ArrayList<>();
        params.add(jobName);
        params.add("running");
        params.add(new java.sql.Date(new java.util.Date().getTime()).toString());
        params.add("0");
        boolean result = conn.upsertData(SQLQueries.insertJobDetail, params);
    }
    
    public List<String> getCategories(){
    	List<String> cats  = new ArrayList<>();
    	ResultSet rs  = conn.executeQuery(SQLQueries.getCategories,null);
    	try {
			while(rs.next()){
				cats.add(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return cats;
    }
}
