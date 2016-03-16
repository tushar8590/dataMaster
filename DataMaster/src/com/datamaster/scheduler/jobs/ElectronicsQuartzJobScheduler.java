package com.datamaster.scheduler.jobs;


import java.util.HashMap;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.datamaster.bl.MSPUrlExtractor;

public class ElectronicsQuartzJobScheduler {
    private static Scheduler sched;
    private Map<String,JobDetail> jobMap ;
    
    
    public ElectronicsQuartzJobScheduler(){
    	initiateJobMap();
    }
    public void initiateJobMap(){
    	jobMap = new HashMap<>();
    	// MSPUrlExtractor Job
    	JobDetail urlExtJob = JobBuilder.newJob(MSPUrlExtractor.class)
                .withIdentity("urlExtractorJob", "group1").build();
    	
    	JobDetail demoJob = JobBuilder.newJob(DemoJob.class)
                .withIdentity("demoJob", "group1").build();
    	
    	jobMap.put("mspUrlExtractorJob",urlExtJob);
    	jobMap.put("demoJob",demoJob);

    }
    
    public void execute(JobDetail job) throws SchedulerException, InterruptedException{
     // simple trigger
        Trigger trigger = TriggerBuilder
            .newTrigger()
            .withIdentity("dummyTriggerName", "group1")
            .withSchedule(
                CronScheduleBuilder.cronSchedule("0/0 * * * * ?"))
            .build();
        
        /*SimpleTrigger trigger = (SimpleTrigger) newTrigger() 
        	    .withIdentity("trigger1", "group1")
        	    .startAt(myStartTime) // some Date 
        	    .forJob("job1", "group1") // identify job with name, group strings
        	    .build();*/
   // schedule it
        Thread.sleep(2000);
                        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
                        scheduler.start();
                        scheduler.scheduleJob(job, trigger);

                     
    }
    
    public void startJob(String jobName,String cats[]){
    	JobDetail job = jobMap.get(jobName);
    	job.getJobDataMap().put("cats", cats);
    	try {
			this.execute(job);
		} catch (SchedulerException | InterruptedException e) {
			
			e.printStackTrace();
		}
    }
    
}
