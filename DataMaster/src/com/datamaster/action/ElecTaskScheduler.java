package com.datamaster.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.quartz.SchedulerException;

import com.datamaster.bl.MSPUrlExtractor;
import com.datamaster.dao.ELectronicsDataDao;
import com.datamaster.scheduler.jobs.ElectronicsQuartzJobScheduler;
import com.opensymphony.xwork2.ActionSupport;

public class ElecTaskScheduler extends ActionSupport implements ServletRequestAware{
    private HttpServletRequest request;
    private String message;
    private ELectronicsDataDao elecDao;
    
    public String execute(){
        ElectronicsQuartzJobScheduler jobSch  =  new ElectronicsQuartzJobScheduler();

        elecDao = new ELectronicsDataDao();
        if(request.getParameter("choice").equals("edu")){
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
        else if(request.getParameter("choice").equals("mpu")){
        	String arr[] = request.getParameterValues("catSelected");
        	MSPUrlExtractor msp = new MSPUrlExtractor();
        	msp.processData(arr);
        	for(String s:arr){
        		System.out.println(s);
        	}
        	//System.out.println();
            return "mspProductUpdate";
        }
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
