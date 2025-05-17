package com.cisystem.ci.domain.mapper;

import com.cisystem.ci.domain.entity.JobDefinition;
import com.cisystem.ci.domain.entity.Trigger;
import org.springframework.stereotype.Component;

@Component
public class JobDefinitionMapper {

    public JobDefinition toJobDefinition(Trigger trigger) {
        return JobDefinition.builder()
                .triggerId(trigger.getId().toString())
                .name(trigger.getName())
                .cron(trigger.getCron())
                .destinationQueue(trigger.getDestinationQueue())
                .build();
    }
}
