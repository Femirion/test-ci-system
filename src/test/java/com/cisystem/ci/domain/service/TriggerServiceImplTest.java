package com.cisystem.ci.domain.service;

import com.cisystem.ci.domain.entity.*;
import com.cisystem.ci.domain.mapper.JobDefinitionMapper;
import com.cisystem.ci.domain.mapper.SignalMapper;
import com.cisystem.ci.port.db.TriggerRepository;
import one.tomorrow.transactionaloutbox.service.OutboxService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TriggerServiceImplTest {

    @Mock
    private TriggerRepository repository;
    @Mock
    private TriggerScheduler scheduler;
    @Mock
    private JobDefinitionMapper jobMapper;
    @Mock
    private OutboxService outboxService;
    @Mock
    private SignalMapper signalMapper;

    @InjectMocks
    private TriggerServiceImpl sut;

    @Test
    void getTrigger_shouldGetTriggerById() {
        UUID triggerId = UUID.randomUUID();

        sut.getTrigger(triggerId);

        verify(repository, times(1)).findById(triggerId);
    }

    @Test
    void getTriggers_shouldGetTriggersByTime() {
        ZonedDateTime now = ZonedDateTime.now();

        sut.getTriggers(now);

        verify(repository, times(1)).findTop10ByCreatedAtBeforeOrderByCreatedAtDesc(now);
    }

    @Test
    void getTriggers_shouldGetTriggersByRepoAndBranch() {

        sut.getTriggers("repo", "branch");

        verify(repository, times(1)).findTriggersByRepositoryAndBranch("repo", "branch");
    }

    @Test
    void createTrigger_shouldSaveTriggerWithoutSchedule() {
        Trigger trigger = Trigger.builder()
                .type(TriggerType.EVENT)
                .destinationQueue("q")
                .build();

        when(repository.save(trigger)).thenReturn(trigger);

        Trigger saved = sut.createTrigger(trigger);

        verify(repository, times(1)).save(trigger);
        verifyNoInteractions(scheduler, jobMapper);
        verify(outboxService, never()).saveForPublishing(anyString(), anyString(), any());
        assertEquals(trigger, saved);
    }

    @Test
    void createTrigger_shouldCreateScheduledTrigger() {
        Trigger trigger = Trigger.builder()
                .id(UUID.randomUUID())
                .type(TriggerType.SCHEDULE)
                .destinationQueue("q")
                .name("myTrigger")
                .build();

        when(repository.save(any())).thenReturn(trigger);
        when(jobMapper.toJobDefinition(trigger)).thenReturn(new JobDefinition("cron", "job", "id", "q"));
        when(signalMapper.toOutboxedSignal(trigger)).thenReturn(new SignalMessage());
        when(scheduler.createJob(any())).thenReturn(new JobKeyInfo("j", "g"));

        Trigger result = sut.createTrigger(trigger);

        assertEquals("j", result.getJobName());
        assertEquals("g", result.getGroupName());
        verify(outboxService).saveForPublishing(eq("q"), anyString(), any());
    }

    @Test
    void removeTrigger_shouldRemoveTrigger() {
        UUID triggerId = UUID.randomUUID();
        Trigger trigger = new Trigger();
        trigger.setId(triggerId);
        trigger.setJobName("myTrigger");
        trigger.setGroupName("myGroup");
        when(repository.findById(triggerId)).thenReturn(Optional.of(trigger));

        sut.removeTrigger(triggerId);

        verify(repository, times(1)).findById(triggerId);
        verify(repository, times(1)).delete(trigger);
        verify(scheduler, times(1)).removeJob(trigger.getJobName(), trigger.getGroupName());
    }
}
