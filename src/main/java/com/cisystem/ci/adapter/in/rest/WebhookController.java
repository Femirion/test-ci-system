package com.cisystem.ci.adapter.in.rest;

import com.cisystem.ci.adapter.in.rest.request.WebHookRequest;
import com.cisystem.ci.domain.provider.TriggerProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class WebhookController {
    private final TriggerProvider triggerProvider;


    @PostMapping("/v1/webhook")
    public void webhook(WebHookRequest webHookRequest) {
        triggerProvider.activateTriggers(webHookRequest.getRepository(), webHookRequest.getBranch());
    }
}
