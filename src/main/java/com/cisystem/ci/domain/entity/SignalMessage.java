package com.cisystem.ci.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SignalMessage {
    private String queue;
    private ZonedDateTime createdAt;
    private String reason;
    private UUID triggerId;
}
