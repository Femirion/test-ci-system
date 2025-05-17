package com.cisystem.ci.port.quartz;

import com.cisystem.ci.domain.entity.SignalMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.tomorrow.transactionaloutbox.service.OutboxService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuartzJob implements Job {
    private final OutboxService outboxService;

    @Transactional
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String triggerId = context.getJobDetail().getJobDataMap().getString("triggerId");
        String name = context.getJobDetail().getJobDataMap().getString("name");
        String destinationQueue = context.getJobDetail().getJobDataMap().getString("destinationQueue");
        outboxService.saveForPublishing(destinationQueue, triggerId, SignalMessage.builder()
                        .queue(destinationQueue)
                        .triggerId(UUID.fromString(triggerId))
                        .reason("SCHEDULED")
                        .build()
                .toString().getBytes());
        log.debug("The sending message was triggered by schedule. Trigger={}, name: {}, destinationQueue: {}", triggerId, name, destinationQueue);
    }
}
