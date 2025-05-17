package com.cisystem.ci.domain.mapper;

import com.cisystem.ci.domain.entity.JobDefinition;
import com.cisystem.ci.domain.entity.Trigger;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JobDefinitionMapperTest {

    private final JobDefinitionMapper sut = new JobDefinitionMapper();

    @Test
    void shouldMapTriggerToJobDefinition() {
        Trigger trigger = Trigger.builder()
                .id(UUID.randomUUID())
                .name("Test")
                .cron("0 0 * * * ?")
                .destinationQueue("queue")
                .build();

        // system under tests
        JobDefinition def = sut.toJobDefinition(trigger);

        assertEquals("Test", def.getName());
        assertEquals("queue", def.getDestinationQueue());
        assertEquals("0 0 * * * ?", def.getCron());
        assertEquals(trigger.getId().toString(), def.getTriggerId());
    }
}