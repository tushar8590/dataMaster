package com.datamaster.scheduler.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class DemoJob implements Job{
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        System.out.println("Hello");
        try {
            Thread.sleep(10000);
        }
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

}
