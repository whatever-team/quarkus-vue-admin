package xyz.easyboot.task;

import io.quarkus.runtime.StartupEvent;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 * @author wujiawei
 * @see
 * @since 2021/7/25 7:36 下午
 */
@ApplicationScoped
public class DemoQuarkusTask {
    
    @Inject
    Scheduler quartz;
    
    void onStart(@Observes StartupEvent event) throws SchedulerException {
        JobDetail job = JobBuilder.newJob(MyJob.class).withIdentity("myJob", "myGroup").build();
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("myTrigger", "myGroup").startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(3).repeatForever()).build();
        quartz.scheduleJob(job, trigger);
    }
    
    @Slf4j
    public static class MyJob implements Job {
        
        public void execute(JobExecutionContext context) {
            log.info("This is demo quartz task");
        }
        
    }
    
    
}
