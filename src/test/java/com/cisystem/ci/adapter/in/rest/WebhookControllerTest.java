package com.cisystem.ci.adapter.in.rest;

import com.cisystem.ci.adapter.in.rest.request.WebHookRequest;
import com.cisystem.ci.domain.provider.TriggerProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WebhookControllerTest {
    @Mock
    private TriggerProvider triggerProvider;
    @InjectMocks
    private WebhookController sut;

    @Test
    void webhook_shouldActivateTriggers() {
        WebHookRequest request = WebHookRequest.builder()
                .repository("my-repo")
                .branch("main")
                .build();

        sut.webhook(request);

        verify(triggerProvider).activateTriggers("my-repo", "main");
    }
}