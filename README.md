# lmpro

`lmpro` is a Spring Boot 4 sample service for account-to-account fund transfers.

## Tech Stack

- Java 21
- Spring Boot 4
- Spring MVC
- Jakarta Validation
- Lombok

## API

### Transfer Funds

`POST /api/accounts/transfer`

Sample request:

```json
{
  "fromUserId": "U1",
  "toUserId": "U2",
  "amount": 100
}
```

Sample success response:

```json
{
  "fromAccount": "A1",
  "toAccount": "A2",
  "amount": 100
}
```

## Exception Handling

The application now uses centralized exception handling through `GlobalExceptionHandler`.

Handled scenarios:

- `BusinessException` and its subclasses such as `UserNotFoundException` and `InsufficientFundsException`
- Validation failures for invalid request payloads
- Bad request errors such as malformed JSON or invalid transfer rules
- Unhandled system exceptions with a safe generic response

Standard error response format:

```json
{
  "timestamp": "2026-04-25T14:30:00Z",
  "code": 4004,
  "errorType": "USER_NOT_FOUND",
  "message": "User not found with ID: U9",
  "method": "POST",
  "path": "/api/accounts/transfer",
  "traceId": "7e5d4c5e-f1d6-4f13-b50d-b1131b6b87d5",
  "details": {
    "userId": "U9",
    "resourceType": "user"
  }
}
```

Current error codes:

- `4004` `USER_NOT_FOUND`
- `6001` `INSUFFICIENT_FUNDS`
- `7001` `INVALID_TRANSFER_REQUEST`
- `7003` `VALIDATION_ERROR`
- `5000` `UNKNOWN_ERROR`

## Validation Rules

The transfer request is validated before reaching business logic.

- `fromUserId` is required
- `toUserId` is required
- `amount` is required
- `amount` must be greater than `0`
- `fromUserId` and `toUserId` must be different

## Logging

The application now includes standard request and service logging.

- Each request gets a generated trace ID
- The trace ID is stored in MDC and returned in the `X-Trace-Id` response header
- Incoming requests and completed responses are logged with status and duration
- Transfer operations are logged at the controller and service layers
- Console logging includes the trace ID for easier troubleshooting

Example log flow:

```text
2026-04-25 20:30:00.123 INFO  [http-nio-8080-exec-1] [7e5d4c5e-f1d6-4f13-b50d-b1131b6b87d5] c.o.l.web.RequestLoggingFilter - Incoming request method=POST path=/api/accounts/transfer
2026-04-25 20:30:00.140 INFO  [http-nio-8080-exec-1] [7e5d4c5e-f1d6-4f13-b50d-b1131b6b87d5] c.o.l.web.AccountController - Received transfer request fromUserId=U1 toUserId=U2 amount=100
2026-04-25 20:30:00.158 INFO  [http-nio-8080-exec-1] [7e5d4c5e-f1d6-4f13-b50d-b1131b6b87d5] c.o.l.service.AccountService - Transfer completed fromAccount=A1 toAccount=A2 amount=100
2026-04-25 20:30:00.166 INFO  [http-nio-8080-exec-1] [7e5d4c5e-f1d6-4f13-b50d-b1131b6b87d5] c.o.l.web.RequestLoggingFilter - Completed request method=POST path=/api/accounts/transfer status=200 durationMs=43
```

## Tests

Test coverage was added for:

- standardized business error responses
- validation error responses
- invalid transfer rule responses
- trace ID response header presence

Note: test execution is currently blocked in this workspace because the Maven wrapper files are missing under `.mvn/wrapper`, and `mvn` is not installed globally.
