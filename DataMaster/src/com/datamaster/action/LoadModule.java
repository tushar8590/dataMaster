package com.datamaster.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.datamaster.dao.ELectronicsDataDao;
import com.opensymphony.xwork2.ActionSupport;

public class LoadModule extends ActionSupport implements ServletRequestAware {
    private HttpServletRequest request;
    private ELectronicsDataDao electronicsData;
    private List<String> categoryList;
    public String execute(){
    	electronicsData = new ELectronicsDataDao();
        if(request.getParameter("choice").equals("electronics")){
        	categoryList  = electronicsData.getCategories();
        	
            return "electronics";
        }else
            return "others";
    }

    @Override
    public void setServletRequest(HttpServletRequest arg0) {
        request = arg0;
        
    }

	public List<String> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<String> categoryList) {
		this.categoryList = categoryList;
	}
}
