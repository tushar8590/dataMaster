package com.datamaster.dao;

import java.util.ArrayList;
import java.util.List;

import com.datamaster.model.JobStatus;

public class JobStatusDao {
    
    private JobStatus jobStatus;
    private JDBCConnection conn;
    public JobStatusDao(){
    	
    }
    public JobStatus getjobStatus(String taskName){
        jobStatus = new JobStatus();
        jobStatus.setJobName("My Job");
        jobStatus.setJobStatus("Runing");
        //List<JobStatus> list = new ArrayList<>();
        //list.add(jobStatus);
        //return list;
        return jobStatus;
    }
}
