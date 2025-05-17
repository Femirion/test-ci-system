package com.cisystem.ci.config;

import one.tomorrow.transactionaloutbox.repository.OutboxRepository;
import one.tomorrow.transactionaloutbox.service.DefaultKafkaProducerFactory;
import one.tomorrow.transactionaloutbox.service.OutboxProcessor;
import one.tomorrow.transactionaloutbox.tracing.TracingService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = "one.tomorrow.transactionaloutbox")
public class TransactionalOutboxConfig {

    private Duration processingInterval = Duration.ofMillis(200);
    private Duration outboxLockTimeout = Duration.ofSeconds(5);
    private String lockOwnerId = lockOwnerId();
    private String eventSource = "my-service";
    private OutboxProcessor.CleanupSettings cleanupSettings = OutboxProcessor.CleanupSettings.builder()
            .interval(Duration.ofDays(1))
            .retention(Duration.ofDays(30))
            .build();

    @Bean
    public OutboxProcessor outboxProcessor(
            OutboxRepository repository,
            Map<String, Object> producerProps,
            TracingService tracingService,
            AutowireCapableBeanFactory beanFactory
    ) {
        producerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        return new OutboxProcessor(
                repository,
                new DefaultKafkaProducerFactory(producerProps),
                processingInterval,
                outboxLockTimeout,
                lockOwnerId,
                eventSource,
                cleanupSettings,
                tracingService,
                beanFactory
        );
    }

    private static String lockOwnerId() {
        try { return InetAddress.getLocalHost().getHostName(); }
        catch (UnknownHostException e) { throw new RuntimeException(e); }
    }

}
