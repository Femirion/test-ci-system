package com.cisystem.ci.domain.mapper;

import com.cisystem.ci.domain.dto.TriggerDto;
import com.cisystem.ci.domain.entity.Trigger;
import com.cisystem.ci.domain.entity.TriggerType;
import org.springframework.stereotype.Component;

@Component()
public class TriggerMapper {

    public Trigger toEntity(TriggerDto dto) {
        return Trigger.builder()
                .name(dto.getName())
                .type(TriggerType.valueOf(dto.getType()))
                .createdBy(dto.getCreatedBy())
                .repository(dto.getRepository())
                .branch(dto.getBranch())
                .destinationQueue(dto.getDestinationQueue())
                .cron(dto.getCron())
                .build();
    }

    public TriggerDto toDto(Trigger entity) {
        return TriggerDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .type(entity.getType().name())
                .repository(entity.getRepository())
                .branch(entity.getBranch())
                .destinationQueue(entity.getDestinationQueue())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
