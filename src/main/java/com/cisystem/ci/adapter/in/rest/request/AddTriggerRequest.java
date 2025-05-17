package com.cisystem.ci.adapter.in.rest.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddTriggerRequest {
    private String name;
    private String desc;
    private String type;
    private String createdBy;
    private String repository;
    private String branch;
    private String destinationQueue;
    private String cron;
}
