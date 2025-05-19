# Simple CI System – Test Project

This is a test project for implementing a simple Continuous Integration (CI) system.

## Project Overview

The system consists of three main components:

1. **Trigger Creation**  
   Triggers can be created to activate builds based on either events (webhooks) or scheduled time (cron expressions).

2. **Message Dispatching**  
   Messages are dispatched to a queue using **Apache Kafka** and the **Transactional Outbox Pattern**, ensuring reliable and consistent delivery.

3. **Stub Webhook Handler**  
   A stub implementation for webhook event handling is included. It looks for matching triggers and sends an event to the configured queue.

## Architecture

The system follows **Clean Architecture** principles:
- Clear separation between core logic, use cases, and infrastructure
- Emphasis on maintainability and testability
- Infrastructure components (Kafka, database, scheduler) are decoupled from business logic

## Technologies Used

- Java 17
- Spring Boot 3
- Apache Kafka
- PostgreSQL
- Quartz Scheduler

---

## Example Requests

### Create a Scheduled Trigger

```http
POST http://localhost:8080/triggers
Content-Type: application/json

{
  "name": "scheduled trigger",
  "type": "SCHEDULE",
  "createdBy": "Alex V",
  "cron": "0 0/1 * * * ?",
  "destinationQueue": "test-queue1"
}
```

### Create a Trigger for Web-Hook

```http
POST http://localhost:8080/triggers
Content-Type: application/json

{
  "name": "web-hook trigger",
  "type": "EVENT",
  "createdBy": "Alex V",
  "destinationQueue": "test-queue2"
}
```

### Get the Latest 10 Triggers (sorted by creation time)

```http
GET http://localhost:8080/triggers
```

### Get Triggers Created After a Specific Time (for pagination)

```http
GET http://localhost:8080/triggers?startFrom=2025-05-17T14:24:39.403162Z

```

### Get a Trigger by ID
```http
GET http://localhost:8080/triggers/25ac326e-506b-47ed-a2f0-807895a1ba04

```

### Delete a Trigger by ID
```http
DELETE http://localhost:8080/triggers/f54d631c-84d9-4d89-9a78-cb920d0abb1e

```
### Stub Webhook Endpoint
When a webhook is received, the system checks if a trigger is registered for the given repository and branch. If found, an event is sent to the configured queue.

Example Webhook Request
```http
POST http://localhost:8080/v1/webhook
Content-Type: application/json

{
  "source": "GITHUB",
  "repository": "test-repo",
  "branch": "main"
}

```
This project is a foundation for experimenting with CI concepts, messaging systems, and Clean Architecture best practices.