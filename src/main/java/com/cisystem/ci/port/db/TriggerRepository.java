package com.cisystem.ci.port.db;

import com.cisystem.ci.domain.entity.Trigger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TriggerRepository extends JpaRepository<Trigger, UUID> {
    List<Trigger> findTriggersByRepositoryAndBranch(String repository, String branch);
    List<Trigger> findTop10ByCreatedAtBeforeOrderByCreatedAtDesc(ZonedDateTime createdAt);
}
