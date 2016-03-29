package com.datamaster.scheduler.jobs;


import java.util.Date;
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

import com.datamaster.bl.MSPCatDataExtractor;
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
    	
    	JobDetail demoJob = JobBuilder.newJob(MSPCatDataExtractor.class)
                .withIdentity("mspCatExtractor", "group1").build();
    	
    	jobMap.put("mspUrlExtractorJob",urlExtJob);
    	jobMap.put("mspCatExtractor",demoJob);

    }
    
    public void execute(JobDetail job) throws SchedulerException, InterruptedException{
     // simple trigger
        Trigger trigger = TriggerBuilder
            .newTrigger()
            .withIdentity("dummyTriggerName"+job.getKey().toString(), "group1")
            .withSchedule(
                CronScheduleBuilder.cronSchedule("0 32 18 * * ? 2016"))
            .build();
        
       
        SimpleTrigger trigger2 = (SimpleTrigger) TriggerBuilder.newTrigger() 
        	    .withIdentity("trigger2"+job.getKey().toString().split("\\.")[1])
        	    .startAt(new Date())// some Date 
        	    .build();
        
   // schedule it
        Thread.sleep(2000);
                        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
                        scheduler.start();
                        scheduler.scheduleJob(job, trigger2);

                     
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
