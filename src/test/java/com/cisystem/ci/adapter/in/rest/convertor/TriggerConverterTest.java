package com.cisystem.ci.adapter.in.rest.convertor;

import com.cisystem.ci.adapter.in.rest.request.AddTriggerRequest;
import com.cisystem.ci.adapter.in.rest.response.TriggerResponse;
import com.cisystem.ci.domain.dto.TriggerDto;
import com.cisystem.ci.domain.entity.TriggerType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TriggerConverterTest {

    private final TriggerConverter converter = new TriggerConverter();

    @Test
    void toResponse_shouldMapTriggerDtoToTriggerResponse() {
        UUID id = UUID.randomUUID();
        TriggerDto dto = TriggerDto.builder()
                .id(id)
                .name("Test Trigger")
                .type(TriggerType.SCHEDULE.name())
                .createdBy("user1")
                .updatedBy("user2")
                .cron("0 0 * * *")
                .repository("repo")
                .branch("main")
                .destinationQueue("queue")
                .build();

        TriggerResponse response = converter.toResponse(dto);

        assertEquals(dto.getId(), response.getId());
        assertEquals(dto.getName(), response.getName());
        assertEquals(dto.getType(), response.getType());
        assertEquals(dto.getCreatedBy(), response.getCreatedBy());
        assertNull(response.getCreatedAt());
        assertEquals(dto.getUpdatedBy(), response.getUpdatedBy());
        assertNull(response.getUpdatedAt());
        assertEquals(dto.getCron(), response.getCron());
        assertEquals(dto.getRepository(), response.getRepository());
        assertEquals(dto.getBranch(), response.getBranch());
        assertEquals(dto.getDestinationQueue(), response.getDestinationQueue());
    }

    @Test
    void toDto_shouldMapAddTriggerRequestToTriggerDto() {
        AddTriggerRequest request = AddTriggerRequest.builder()
                .name("New Trigger")
                .type(TriggerType.EVENT.name())
                .repository("repo2")
                .branch("dev")
                .destinationQueue("dev-queue")
                .createdBy("creator")
                .build();

        TriggerDto dto = converter.toDto(request);

        assertNull(dto.getId());
        assertEquals(dto.getName(), request.getName());
        assertEquals(dto.getType(), request.getType());
        assertEquals(dto.getCreatedBy(), request.getCreatedBy());
        assertNull(dto.getCreatedAt());
        assertNull(dto.getUpdatedAt());
        assertEquals(dto.getCron(), request.getCron());
        assertEquals(dto.getRepository(), request.getRepository());
        assertEquals(dto.getBranch(), request.getBranch());
        assertEquals(dto.getDestinationQueue(), request.getDestinationQueue());
    }
}