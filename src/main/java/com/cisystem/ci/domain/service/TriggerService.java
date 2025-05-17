package com.cisystem.ci.domain.service;

import com.cisystem.ci.domain.entity.Trigger;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TriggerService {
    Trigger createTrigger(Trigger trigger);
    Optional<Trigger> getTrigger(UUID id);
    List<Trigger> getTriggers(ZonedDateTime startFrom);
    List<Trigger> getTriggers(String repository, String branch);
    void removeTrigger(UUID id);
}
