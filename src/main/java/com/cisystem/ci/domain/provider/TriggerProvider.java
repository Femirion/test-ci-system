package com.cisystem.ci.domain.provider;

import com.cisystem.ci.domain.dto.TriggerDto;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public interface TriggerProvider {
    TriggerDto getTrigger(UUID triggerId);
    List<TriggerDto> getTriggers(ZonedDateTime from);
    TriggerDto createTrigger(TriggerDto triggerDto);
    void removeTrigger(UUID id);
    void activateTriggers(String repository, String branch);
}
