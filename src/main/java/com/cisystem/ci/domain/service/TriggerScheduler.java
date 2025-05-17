package com.cisystem.ci.domain.service;

import com.cisystem.ci.domain.entity.JobDefinition;
import com.cisystem.ci.domain.entity.JobKeyInfo;

public interface TriggerScheduler {
    JobKeyInfo createJob(JobDefinition definition);
    void removeJob(String jobName, String groupName);
}
