package com.cisystem.ci.adapter.in.rest.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TriggerResponse {
    private UUID id;
    private String name;
    private String type;
    private ZonedDateTime createdAt;
    private String createdBy;
    private ZonedDateTime updatedAt;
    private String updatedBy;
    private String cron;
    private String repository;
    private String branch;
    private String destinationQueue;
}