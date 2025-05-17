package com.cisystem.ci.adapter.in.rest.convertor;

import com.cisystem.ci.adapter.in.rest.request.AddTriggerRequest;
import com.cisystem.ci.adapter.in.rest.response.TriggerResponse;
import com.cisystem.ci.domain.dto.TriggerDto;
import org.springframework.stereotype.Component;

@Component
public class TriggerConverter {

    public TriggerResponse toResponse(TriggerDto trigger) {
        return TriggerResponse.builder()
                .id(trigger.getId())
                .name(trigger.getName())
                .type(trigger.getType())
                .createdBy(trigger.getCreatedBy())
                .createdAt(trigger.getCreatedAt())
                .updatedBy(trigger.getUpdatedBy())
                .updatedAt(trigger.getUpdatedAt())
                .cron(trigger.getCron())
                .repository(trigger.getRepository())
                .branch(trigger.getBranch())
                .destinationQueue(trigger.getDestinationQueue())
                .build();
    }

    public TriggerDto toDto(AddTriggerRequest trigger) {
        return TriggerDto.builder()
                .name(trigger.getName())
                .type(trigger.getType())
                .repository(trigger.getRepository())
                .branch(trigger.getBranch())
                .destinationQueue(trigger.getDestinationQueue())
                .cron(trigger.getCron())
                .createdBy(trigger.getCreatedBy())
                .build();
    }
}
