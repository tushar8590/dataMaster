package com.datamaster.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

public class LoadModule extends ActionSupport implements ServletRequestAware {
    private HttpServletRequest request;
    
    private List<String> categoryList;
    public String execute(){
        if(request.getParameter("choice").equals("electronics")){
        	categoryList = new ArrayList<String>();
        	categoryList.add("Abc");
        	categoryList.add("CDF");
        	categoryList.add("GGF");
        	categoryList.add("JJH");
        	categoryList.add("TTT");
        	categoryList.add("KKK");
        	
            return "electronics";
        }else
            return "others";
    }

    @Override
    public void setServletRequest(HttpServletRequest arg0) {
        request = arg0;
        
    }
}
