package com.cisystem.ci.port.quartz;

import com.cisystem.ci.domain.entity.JobDefinition;
import com.cisystem.ci.domain.entity.JobKeyInfo;
import com.cisystem.ci.domain.exception.ScheduledJobException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuartzTriggerSchedulerTest {

    @Mock
    private Scheduler scheduler;
    private QuartzTriggerScheduler quartzTriggerScheduler;

    @BeforeEach
    void setup() {
        quartzTriggerScheduler = new QuartzTriggerScheduler(scheduler);
    }

    @Test
    void createJob_shouldCreateJob() throws Exception {
        JobDefinition jobDefinition = JobDefinition.builder()
                .cron("0 0 * * * ?")
                .name("test")
                .destinationQueue("q")
                .triggerId(UUID.randomUUID().toString())
                .build();

        JobKeyInfo info = quartzTriggerScheduler.createJob(jobDefinition);

        assertNotNull(info.getJobName());
        assertNotNull(info.getGroupName());
        verify(scheduler).scheduleJob(any(JobDetail.class), any(Trigger.class));
    }

    @Test
    void createJob_shouldThrowSchedulerException() throws Exception {
        JobDefinition jobDefinition = JobDefinition.builder()
                .cron("0 0 * * * ?")
                .name("test")
                .destinationQueue("q")
                .triggerId("12345")
                .build();
        when(scheduler.scheduleJob(any(), any())).thenThrow(new SchedulerException());

        ScheduledJobException ex = assertThrows(ScheduledJobException.class, () ->  quartzTriggerScheduler.createJob(jobDefinition));

        verify(scheduler).scheduleJob(any(JobDetail.class), any(Trigger.class));
        assertTrue(ex.getMessage().startsWith("Error during creating a Cron job for trigger=12345"));
    }

    @Test
    void removeJob_shouldRemoveJob() throws Exception {
        quartzTriggerScheduler.removeJob("jobName", "groupName");

        verify(scheduler).deleteJob(new JobKey("jobName","groupName"));
    }

    @Test
    void removeJob_shouldThrowSchedulerException() throws Exception {
        when(scheduler.deleteJob(any())).thenThrow(new SchedulerException());

        ScheduledJobException ex = assertThrows(ScheduledJobException.class, () ->  quartzTriggerScheduler.removeJob("jobName1", "groupName1"));

        verify(scheduler).deleteJob(new JobKey("jobName1","groupName1"));
        assertEquals("Can not delete the job, jobName=jobName1, groupName=groupName1", ex.getMessage());
    }


}
