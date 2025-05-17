package com.cisystem.ci.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobDefinition {
    private String cron;
    private String name;
    private String triggerId;
    private String destinationQueue;
}
