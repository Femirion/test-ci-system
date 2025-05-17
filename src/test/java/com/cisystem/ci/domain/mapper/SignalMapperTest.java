package com.cisystem.ci.domain.mapper;

import com.cisystem.ci.domain.entity.SignalMessage;
import com.cisystem.ci.domain.entity.Trigger;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SignalMapperTest {

    private final SignalMapper sut = new SignalMapper();

    @Test
    void shouldMapTriggerToSignalMessage() {
        UUID id = UUID.randomUUID();
        Trigger trigger = Trigger.builder()
                .id(id)
                .name("reason")
                .destinationQueue("q")
                .build();

        SignalMessage signal = sut.toOutboxedSignal(trigger);

        assertEquals(id, signal.getTriggerId());
        assertEquals("reason", signal.getReason());
        assertEquals("q", signal.getQueue());
        assertNull(signal.getCreatedAt());
    }
}