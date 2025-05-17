package com.cisystem.ci.adapter.in.rest;

import com.cisystem.ci.adapter.in.rest.convertor.TriggerConverter;
import com.cisystem.ci.adapter.in.rest.request.AddTriggerRequest;
import com.cisystem.ci.adapter.in.rest.response.TriggerResponse;
import com.cisystem.ci.domain.provider.TriggerProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController("/v1")
public class TriggerController {
    private final TriggerProvider triggerProvider;
    private final TriggerConverter triggerConverter;

    @GetMapping("/triggers")
    public List<TriggerResponse> getTriggers(@Param("startFrom") ZonedDateTime startFrom) {
        if (startFrom == null) {
            startFrom = ZonedDateTime.now();
        }
        return triggerProvider.getTriggers(startFrom).stream()
                .map(triggerConverter::toResponse)
                .toList();
    }

    @GetMapping("/triggers/{id}")
    public TriggerResponse getTrigger(@PathVariable("id") UUID id) {
        return triggerConverter.toResponse(triggerProvider.getTrigger(id));
    }

    @PostMapping("/triggers")
    public TriggerResponse addTrigger(@RequestBody AddTriggerRequest triggerRequest) {
        return triggerConverter.toResponse(triggerProvider.createTrigger(triggerConverter.toDto(triggerRequest)));
    }

    @DeleteMapping("/triggers/{id}")
    public void removeTrigger(@PathVariable("id") UUID id) {
        triggerProvider.removeTrigger(id);
    }
}
