package com.cisystem.ci.adapter.in.rest;

import com.cisystem.ci.adapter.in.rest.convertor.TriggerConverter;
import com.cisystem.ci.adapter.in.rest.request.AddTriggerRequest;
import com.cisystem.ci.adapter.in.rest.response.TriggerResponse;
import com.cisystem.ci.domain.dto.TriggerDto;
import com.cisystem.ci.domain.entity.TriggerType;
import com.cisystem.ci.domain.provider.TriggerProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TriggerControllerTest {
    @Mock
    private TriggerProvider triggerProvider;
    @Mock
    private TriggerConverter triggerConverter;
    @InjectMocks
    private TriggerController sut;

    @Test
    void getTriggers_shouldReturnListOfTriggerResponses() {
        ZonedDateTime now = ZonedDateTime.now();
        TriggerDto dto = TriggerDto.builder().id(UUID.randomUUID()).build();
        TriggerResponse response = TriggerResponse.builder().id(dto.getId()).build();

        when(triggerProvider.getTriggers(any())).thenReturn(List.of(dto));
        when(triggerConverter.toResponse(dto)).thenReturn(response);

        List<TriggerResponse> result = sut.getTriggers(now);

        assertEquals(1, result.size());
        assertEquals(dto.getId(), result.get(0).getId());
        verify(triggerProvider).getTriggers(now);
    }

    @Test
    void getTriggers_shouldUseCurrentTimeIfStartFromIsNull() {
        // Используем ArgumentCaptor, чтобы убедиться, что был передан текущий момент времени
        when(triggerProvider.getTriggers(any())).thenReturn(List.of());
        List<TriggerResponse> result = sut.getTriggers(null);

        assertEquals(0, result.size());
        verify(triggerProvider).getTriggers(any(ZonedDateTime.class));
    }

    @Test
    void getTrigger_shouldReturnTriggerResponse() {
        UUID id = UUID.randomUUID();
        TriggerDto dto = TriggerDto.builder().id(id).build();
        TriggerResponse response = TriggerResponse.builder().id(id).build();

        when(triggerProvider.getTrigger(id)).thenReturn(dto);
        when(triggerConverter.toResponse(dto)).thenReturn(response);

        TriggerResponse result = sut.getTrigger(id);

        assertEquals(id, result.getId());
        verify(triggerProvider).getTrigger(id);
    }

    @Test
    void addTrigger_shouldConvertAndReturnTriggerResponse() {
        AddTriggerRequest request = AddTriggerRequest.builder()
                .name("New Trigger")
                .type(TriggerType.SCHEDULE.name())
                .build();

        TriggerDto dto = TriggerDto.builder()
                .id(UUID.randomUUID())
                .name("New Trigger")
                .type(TriggerType.SCHEDULE.name())
                .build();

        TriggerResponse response = TriggerResponse.builder()
                .id(dto.getId())
                .name("New Trigger")
                .type(TriggerType.SCHEDULE.name())
                .build();

        when(triggerConverter.toDto(request)).thenReturn(dto);
        when(triggerProvider.createTrigger(dto)).thenReturn(dto);
        when(triggerConverter.toResponse(dto)).thenReturn(response);

        TriggerResponse result = sut.addTrigger(request);

        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getName(), result.getName());
        verify(triggerProvider).createTrigger(dto);
    }

    @Test
    void removeTrigger_shouldCallProvider() {
        UUID id = UUID.randomUUID();

        sut.removeTrigger(id);

        verify(triggerProvider).removeTrigger(id);
    }
}