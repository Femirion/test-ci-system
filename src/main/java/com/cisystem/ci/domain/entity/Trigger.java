package com.cisystem.ci.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "triggers")
public class Trigger {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "name", nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TriggerType type;
    @Column(name = "repository")
    private String repository;
    @Column(name = "branch")
    private String branch;
    @Column(name = "destination_queue", nullable = false)
    private String destinationQueue;
    @Column(name = "cron")
    private String cron;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;
    @Column(name = "created_by")
    private String createdBy;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
    @Column(name = "updated_by")
    private String updatedBy;
    @Column(name = "job_name")
    private String jobName;
    @Column(name = "group_name")
    private String groupName;
}