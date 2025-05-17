package com.cisystem.ci.domain.mapper;

import com.cisystem.ci.domain.entity.SignalMessage;
import com.cisystem.ci.domain.entity.Trigger;
import org.springframework.stereotype.Component;

@Component
public class SignalMapper {

    public SignalMessage toOutboxedSignal(Trigger trigger) {
        return SignalMessage.builder()
                .queue(trigger.getDestinationQueue())
                .reason(trigger.getName())
                .triggerId(trigger.getId())
                .build();
    }
}
