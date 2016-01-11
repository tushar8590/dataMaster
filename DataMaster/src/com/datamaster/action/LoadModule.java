package com.datamaster.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

public class LoadModule extends ActionSupport implements ServletRequestAware {
    private HttpServletRequest request;
    public String execute(){
        if(request.getParameter("choice").equals("electronics"))
            return "electronics";
        else
            return "others";
    }

    @Override
    public void setServletRequest(HttpServletRequest arg0) {
        request = arg0;
        
    }
}
