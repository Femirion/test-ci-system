package com.cisystem.ci.adapter.in.rest.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebHookRequest {
    private String source;
    private String repository;
    private String branch;
}
