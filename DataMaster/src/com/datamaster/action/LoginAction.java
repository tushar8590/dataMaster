package com.datamaster.action;

import com.opensymphony.xwork2.ActionSupport;
import com.datamaster.model.*;
public class LoginAction extends ActionSupport {
    private User user;
    
    public String execute(){
        if(user.getUsername().equalsIgnoreCase("admin") && user.getPassword().equals("admin")){
            return "success"; 
        }
       addActionError("Invalid User name or password");
        return "error";
       
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
