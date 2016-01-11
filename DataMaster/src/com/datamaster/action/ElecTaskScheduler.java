package com.datamaster.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

public class ElecTaskScheduler extends ActionSupport implements ServletRequestAware{
    private HttpServletRequest request;
    public String execute(){
        if(request.getParameter("choice").equals("edu"))
            return "electronicsDataUpdate";
        else if(request.getParameter("choice").equals("mpu"))
            return "mspProductUpdate";
        else
            return "specUpdate";
    }
    @Override
    public void setServletRequest(HttpServletRequest arg0) {
        request = arg0;
        
    }
    
}
