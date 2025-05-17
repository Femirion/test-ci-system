package com.cisystem.ci.domain.provider;

import com.cisystem.ci.domain.dto.TriggerDto;
import com.cisystem.ci.domain.entity.Trigger;
import com.cisystem.ci.domain.mapper.SignalMapper;
import com.cisystem.ci.domain.mapper.TriggerMapper;
import com.cisystem.ci.domain.service.TriggerService;
import lombok.RequiredArgsConstructor;
import one.tomorrow.transactionaloutbox.service.OutboxService;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TriggerProviderImpl implements TriggerProvider {
    private final TriggerService triggerService;
    private final TriggerMapper mapper;
    private final OutboxService outboxService;
    private final SignalMapper signalMapper;

    @Override
    public TriggerDto getTrigger(UUID triggerId) {
        return triggerService.getTrigger(triggerId)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    public List<TriggerDto> getTriggers(ZonedDateTime from) {
        return triggerService.getTriggers(from).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public TriggerDto createTrigger(TriggerDto triggerDto) {
        var entity = mapper.toEntity(triggerDto);
        var createdTrigger = triggerService.createTrigger(entity);
        return mapper.toDto(createdTrigger);
    }

    @Override
    public void removeTrigger(UUID id) {
        triggerService.removeTrigger(id);
    }

    @Override
    public void activateTriggers(String repository, String branch) {
        for (Trigger trigger : triggerService.getTriggers(repository, branch)) {
            outboxService.saveForPublishing(trigger.getDestinationQueue(), trigger.getId().toString(),
                    signalMapper.toOutboxedSignal(trigger).toString().getBytes());
        }

    }
}
