package com.cisystem.ci.domain.provider;

import com.cisystem.ci.domain.dto.TriggerDto;
import com.cisystem.ci.domain.entity.SignalMessage;
import com.cisystem.ci.domain.entity.Trigger;
import com.cisystem.ci.domain.mapper.SignalMapper;
import com.cisystem.ci.domain.mapper.TriggerMapper;
import com.cisystem.ci.domain.service.TriggerService;
import one.tomorrow.transactionaloutbox.service.OutboxService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TriggerProviderImplTest {

    @Mock
    private TriggerService triggerService;
    @Mock
    private TriggerMapper triggerMapper;
    @Mock
    private OutboxService outboxService;
    @Mock
    private SignalMapper signalMapper;

    @InjectMocks
    private TriggerProviderImpl provider;

    @Test
    void shouldCreateTrigger() {
        UUID id = UUID.randomUUID();
        Trigger trigger = Trigger.builder().id(id).build();
        TriggerDto dto = TriggerDto.builder().id(id).build();

        when(triggerMapper.toEntity(any())).thenReturn(trigger);
        when(triggerService.createTrigger(any())).thenReturn(trigger);
        when(triggerMapper.toDto(any())).thenReturn(dto);

        TriggerDto result = provider.createTrigger(dto);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void shouldRemoveTrigger() {
        UUID id = UUID.randomUUID();
        provider.removeTrigger(id);

        verify(triggerService).removeTrigger(id);
    }

    @Test
    void shouldReturnTriggerDto_whenTriggerExists() {
        UUID id = UUID.randomUUID();
        Trigger trigger = Trigger.builder().id(id).build();
        TriggerDto dto = TriggerDto.builder().id(id).build();

        when(triggerService.getTrigger(id)).thenReturn(Optional.of(trigger));
        when(triggerMapper.toDto(trigger)).thenReturn(dto);

        TriggerDto result = provider.getTrigger(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void shouldReturnAllTriggersMapped() {
        Trigger t1 = Trigger.builder().id(UUID.randomUUID()).name("t1").build();
        Trigger t2 = Trigger.builder().id(UUID.randomUUID()).name("t2").build();
        List<Trigger> triggers = List.of(t1, t2);

        TriggerDto d1 = TriggerDto.builder().id(t1.getId()).name("t1").build();
        TriggerDto d2 = TriggerDto.builder().id(t2.getId()).name("t2").build();

        when(triggerService.getTriggers(any())).thenReturn(triggers);
        when(triggerMapper.toDto(t1)).thenReturn(d1);
        when(triggerMapper.toDto(t2)).thenReturn(d2);

        List<TriggerDto> result = provider.getTriggers(ZonedDateTime.now());

        assertEquals(2, result.size());
    }

    @Test
    void shouldSendSignal_whenSignalTriggerCalled() {
        UUID id = UUID.randomUUID();
        Trigger trigger = Trigger.builder()
                .id(id)
                .destinationQueue("queue")
                .build();
        SignalMessage signal = SignalMessage.builder()
                .triggerId(id)
                .queue("queue")
                .reason("manual-signal")
                .build();

        when(triggerService.getTriggers("repository", "branch")).thenReturn(List.of(trigger));
        when(signalMapper.toOutboxedSignal(trigger)).thenReturn(signal);

        provider.activateTriggers("repository", "branch");

        verify(outboxService).saveForPublishing(any(), any(), any());
    }

    @Test
    void shouldThrow_whenSignalTriggerTargetNotFound() {
        when(triggerService.getTriggers("repository", "branch")).thenReturn(List.of());

        provider.activateTriggers("repository", "branch");
        verifyNoInteractions(signalMapper, outboxService);
    }
}
