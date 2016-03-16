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
        String arr[] = request.getParameterValues("catSelected");
        if(request.getParameter("choice").equals("edu")){
            
                elecDao.initiateJob("electronicsDataUpdate");
                jobSch.startJob("demoJob",arr);            
                setMessage("Running Data Update Job");
                return "electronicsDataUpdate";
        }
        else if(request.getParameter("choice").equals("mpu")){
	        //will uncomment later on	elecDao.initiateJob("mspUrlExtractorJob");
	        //	
	        	//MSPUrlExtractor msp = new MSPUrlExtractor(arr);
	        	//msp.processData(arr);
        	 jobSch.startJob("mspUrlExtractorJob",arr);            
             setMessage("Running mspUrlExtractorJob Job");
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
