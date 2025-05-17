package com.cisystem.ci.port.quartz;

import com.cisystem.ci.domain.entity.JobDefinition;
import com.cisystem.ci.domain.entity.JobKeyInfo;
import com.cisystem.ci.domain.exception.ScheduledJobException;
import com.cisystem.ci.domain.service.TriggerScheduler;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class QuartzTriggerScheduler implements TriggerScheduler {

    private final Scheduler scheduler;

    @Override
    public JobKeyInfo createJob(JobDefinition jobDefinition) {
        JobDetail jobDetail = JobBuilder.newJob(QuartzJob.class)
                .withIdentity("job-" + UUID.randomUUID())
                .usingJobData(jobDataMap(jobDefinition))
                .build();

        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger-" + UUID.randomUUID())
                .withSchedule(CronScheduleBuilder.cronSchedule(jobDefinition.getCron()))
                .forJob(jobDetail)
                .build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new ScheduledJobException("Error during creating a Cron job for trigger=" + jobDefinition.getTriggerId(), e);
        }

        return JobKeyInfo.builder()
                .jobName(jobDetail.getKey().getName())
                .groupName(jobDetail.getKey().getGroup())
                .build();
    }

    @Override
    public void removeJob(String jobName, String groupName) {
        try {
            scheduler.deleteJob(new JobKey(jobName, groupName));
        } catch (SchedulerException e) {
            throw new ScheduledJobException("Can not delete the job, jobName=" + jobName + ", groupName=" + groupName, e);
        }
    }

    private JobDataMap jobDataMap(JobDefinition jobDefinition) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("triggerId", jobDefinition.getTriggerId());
        jobDataMap.put("name", jobDefinition.getName());
        jobDataMap.put("destinationQueue", jobDefinition.getDestinationQueue());
        return jobDataMap;
    }
}
