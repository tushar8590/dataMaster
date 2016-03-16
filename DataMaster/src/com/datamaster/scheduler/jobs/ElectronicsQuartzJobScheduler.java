package com.datamaster.scheduler.jobs;


import java.util.HashMap;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class ElectronicsQuartzJobScheduler {
    private static Scheduler sched;
    private Map<JobDetail,Trigger> jobmap ;
    
    public void initiateJobMap(){
    	jobmap = new HashMap<>();
    	// MSPUrlExtractor Job
    	JobDetail job = JobBuilder.newJob(DemoJob.class)
                .withIdentity("dummyJobName", "group1").build();
           
    	
    }
    
    public void execute() throws SchedulerException, InterruptedException{
        
     // simple trigger
        Trigger trigger = TriggerBuilder
            .newTrigger()
            .withIdentity("dummyTriggerName", "group1")
            .withSchedule(
                CronScheduleBuilder.cronSchedule("0/1 * * * * ?"))
            .build();

    // schedule it
        Thread.sleep(2000);
                        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
                        scheduler.start();
                        scheduler.scheduleJob(job, trigger);

                     
    }
    
    
}
