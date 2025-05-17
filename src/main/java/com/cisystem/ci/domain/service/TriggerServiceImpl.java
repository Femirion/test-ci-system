package com.cisystem.ci.domain.service;

import com.cisystem.ci.domain.entity.JobKeyInfo;
import com.cisystem.ci.domain.entity.Trigger;
import com.cisystem.ci.domain.entity.TriggerType;
import com.cisystem.ci.domain.mapper.JobDefinitionMapper;
import com.cisystem.ci.domain.mapper.SignalMapper;
import com.cisystem.ci.port.db.TriggerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.tomorrow.transactionaloutbox.service.OutboxService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class TriggerServiceImpl implements TriggerService {
    private final TriggerRepository triggerRepository;
    private final TriggerScheduler triggerScheduler;
    private final JobDefinitionMapper jobDefinitionMapper;
    private final OutboxService outboxService;
    private final SignalMapper signalMapper;

    @Transactional
    @Override
    public Trigger createTrigger(Trigger trigger) {
        Trigger savedTrigger = triggerRepository.save(trigger);

        if (trigger.getType() != null && TriggerType.SCHEDULE == trigger.getType()) {
            JobKeyInfo keyInfo = triggerScheduler.createJob(jobDefinitionMapper.toJobDefinition(trigger));
            savedTrigger.setJobName(keyInfo.getJobName());
            savedTrigger.setGroupName(keyInfo.getGroupName());
            triggerRepository.save(savedTrigger);
            outboxService.saveForPublishing(trigger.getDestinationQueue(), trigger.getId().toString(), signalMapper.toOutboxedSignal(trigger).toString().getBytes());
        }

        log.info("Trigger created: {}", trigger.getId());
        return savedTrigger;
    }

    @Override
    public Optional<Trigger> getTrigger(UUID id) {
        return triggerRepository.findById(id);
    }

    @Override
    public List<Trigger> getTriggers(ZonedDateTime startFrom) {
        return triggerRepository.findTop10ByCreatedAtBeforeOrderByCreatedAtDesc(startFrom);
    }

    @Override
    public List<Trigger> getTriggers(String repository, String branch) {
        return triggerRepository.findTriggersByRepositoryAndBranch(repository, branch);
    }

    @Transactional
    @Override
    public void removeTrigger(UUID id) {
        Optional<Trigger> trigger = triggerRepository.findById(id);
        trigger.ifPresent(t -> {
            triggerRepository.delete(t);
            triggerScheduler.removeJob(t.getJobName(), t.getGroupName());
        });
    }
}
