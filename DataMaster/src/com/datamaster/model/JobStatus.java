package com.datamaster.model;

import java.io.Serializable;

public class JobStatus implements Serializable{
    private String jobName;
    private String jobStatus;
    public String getJobName() {
        return jobName;
    }
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
    public String getJobStatus() {
        return jobStatus;
    }
    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }
    
    
}
