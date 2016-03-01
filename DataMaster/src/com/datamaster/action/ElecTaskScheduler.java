package com.datamaster.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.quartz.SchedulerException;

import com.datamaster.dao.ELectronicsDataDao;
import com.datamaster.scheduler.jobs.ElectronicsQuartzJobScheduler;
import com.opensymphony.xwork2.ActionSupport;

public class ElecTaskScheduler extends ActionSupport implements ServletRequestAware{
    private HttpServletRequest request;
    private String message;
    private ELectronicsDataDao elecDao;
    
    public String execute(){
        //elecDao = new ELectronicsDataDao();
        if(request.getParameter("choice").equals("edu")){
            ElectronicsQuartzJobScheduler jobSch  =  new ElectronicsQuartzJobScheduler();
            try {
                
                elecDao.initiateJob("electronicsDataUpdate");
                jobSch.execute();
            }
            catch (SchedulerException | InterruptedException e) {
               
                e.printStackTrace();
            }
            
            setMessage("Running Data Update Job");
            return "electronicsDataUpdate";
        }
        else if(request.getParameter("choice").equals("mpu"))
            return "mspProductUpdate";
        else
            return "specUpdate";
    }
    @Override
    public void setServletRequest(HttpServletRequest arg0) {
        request = arg0;
        
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    
}
