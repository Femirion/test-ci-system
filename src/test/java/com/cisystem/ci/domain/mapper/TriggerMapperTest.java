package com.cisystem.ci.domain.mapper;

import com.cisystem.ci.domain.dto.TriggerDto;
import com.cisystem.ci.domain.entity.Trigger;
import com.cisystem.ci.domain.entity.TriggerType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TriggerMapperTest {
    private final TriggerMapper sut = new TriggerMapper();

    @Test
    void shouldMapDtoToEntity() {
        TriggerDto dto = TriggerDto.builder()
                .name("T")
                .type("SCHEDULE")
                .createdBy("me")
                .repository("r")
                .branch("b")
                .destinationQueue("q")
                .cron("* * * * *")
                .build();

        Trigger entity = sut.toEntity(dto);

        assertEquals("T", entity.getName());
        assertEquals(TriggerType.SCHEDULE, entity.getType());
        assertEquals("me", entity.getCreatedBy());
        assertEquals("r", entity.getRepository());
    }

    @Test
    void shouldMapEntityToDto() {
        Trigger entity = Trigger.builder()
                .id(UUID.randomUUID())
                .name("T")
                .type(TriggerType.EVENT)
                .destinationQueue("q")
                .build();

        TriggerDto dto = sut.toDto(entity);

        assertEquals("T", dto.getName());
        assertEquals("EVENT", dto.getType());
    }
}