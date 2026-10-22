---
name: 'Layered Application & Packaging Instructions'
description: 'This file describes the layered application and packaging instructions for all java files.'
applyTo: '**/*.java'
---

# Layered Application & Packaging

## 1. Architecture

The application follows a **3-layer architecture**:

1.  **Controller** - Entry-level/API logic. All `@RestController`
    classes belong here.
    -   The Controller layer can call the Service layer **only through
        Service interfaces**.
2.  **Service** - Core business logic belongs here.
    -   The Service layer can call the Repository layer **only through
        Repository interfaces**.
3.  **Repository** - Database access and persistence logic belongs here.


Layers are implemented using **Java packages**.

------------------------------------------------------------------------

## 2. Base Package Structure

Under the `<base package>` of the project, use the following package
structure.

**Always maximize alignment with the structure defined below.**

If a requirement needs a package that is not explicitly defined here,
make the best decision according to **industry standards**, while
maintaining alignment with the overall package structure.

``` text
<base package>
├── controller
├── service
├── repository
├── constant
├── util
├── config
├── http
├── exception
├── pojo
├── entity
└── dto
```

------------------------------------------------------------------------

## 3. Controller Package

### `.controller`

-   Contains all REST/API controllers.
-   Controllers should primarily handle API entry-point concerns.
-   Avoid placing core business logic directly in controllers.

Example:

``` text
<base package>.controller
```

------------------------------------------------------------------------

## 4. Service Package

### `.service`

Contains the **core business logic** of the application.

Recommended structure:

``` text
.service
├── interfaces
├── impl
├── helper
├── factory
└── processor
```

### `.service.interfaces`

-   Contains service interfaces.

### `.service.impl`

-   Contains implementations of service interfaces.

### `.service` direct classes

-   Classes that are service-related but do not require an
    interface/implementation structure can be placed directly under
    `.service`.

### Service subpackages

Create additional subpackages based on actual need, for example:

-   `.service.helper` - Service-specific helper classes.
-   `.service.factory` - Factory implementations/classes.
-   `.service.processor` - Processing-related classes.

Do not create unnecessary packages only for the sake of package
hierarchy.

------------------------------------------------------------------------

## 5. Repository Package

### `.repository`

Contains database access and persistence logic.

Recommended structure:

``` text
.repository
├── interfaces
└── impl
```

### `.repository.interfaces`

-   Database/repository interfaces.

### `.repository.impl`

-   Repository implementation classes.

------------------------------------------------------------------------

## 6. Constant Package

### `.constant`

Contains application-wide constants and enums.

Examples:

``` text
.constant
├── Constant.java
├── ErrorCodeEnum.java
└── ...
```

-   `Constant.java` - Common static constants.
-   `ErrorCodeEnum` - Error-code related enum.
-   Other application-wide enums/constants belong here when appropriate.

------------------------------------------------------------------------

## 7. Utility Package

### `.util`

Contains reusable utility classes that may be required across different
parts of the project.

Examples:

``` text
JsonUtil
ErrorUtil
```

Utilities should contain generic/reusable functionality rather than
business logic.

------------------------------------------------------------------------

## 8. Configuration Package

### `.config`

Contains Spring `@Configuration` classes where application beans and
framework configuration are defined.

Examples:

``` text
SecurityConfig
RedisConfig
ObjectMapperConfig
```

------------------------------------------------------------------------

## 9. HTTP Package

### `.http`

Contains common classes used for making HTTP calls to external services
or microservices.

Examples:

``` text
HttpServiceEngine
HttpRequest
```

These classes should provide reusable HTTP communication functionality
shared across the project.

------------------------------------------------------------------------

## 10. Exception Package

### `.exception`

Contains application-specific exception handling.

Include:

-   Custom exceptions.
-   `@RestControllerAdvice` classes.
-   Centralized exception-handling logic.

Example:

``` text
.exception
├── CustomException.java
└── GlobalExceptionHandler.java
```

------------------------------------------------------------------------

# 11. Model Layer Objects

Use clear separation between objects used at different application
boundaries.

## 11.1 Incoming API Objects

Objects received from external/API clients.

### `.pojo`

Can contain request/response POJOs.

Recommended structure:

``` text
.pojo
├── req
└── res
```

Examples:

``` text
.pojo.req
.pojo.res
```

-   `.pojo.req` - Incoming request objects.
-   `.pojo.res` - Incoming/API response-related POJOs where applicable.

------------------------------------------------------------------------

## 11.2 Outgoing Service Objects

Objects used when communicating with external services/microservices.

Recommended structure:

``` text
.service
├── req
└── res
```

Examples:

``` text
.service.req
.service.res
```

-   `.service.req` - Outgoing service request objects.
-   `.service.res` - Outgoing service response objects.

> Keep API-facing objects and external-service communication objects
> separated.

------------------------------------------------------------------------

## 11.3 Entity

### `.entity`

Contains database/entity objects mapped to database tables.

Example:

``` text
.entity
└── TransactionEntity.java
```

Entities should represent persistence/database concerns and should not
be unnecessarily exposed directly through APIs.

------------------------------------------------------------------------

## 11.4 DTO

### `.dto`

Contains Data Transfer Objects used to transfer data between application
layers or boundaries where appropriate.

------------------------------------------------------------------------

# 12. Object Conversion Flow

Maintain clear separation between different object models.

Typical flow:

``` text
Incoming API
    ↓
POJO
    ↓
DTO
    ↓
ENTITY
    ↓
Database
```

For outgoing/external-service communication:

``` text
Application
    ↓
Service Request
    ↓
External API
    ↓
Service Response
```

Use **ModelMapper** wherever appropriate for object conversion to reduce
repetitive mapping code.

> ModelMapper usage and detailed mapping guidelines will be documented
> separately.

------------------------------------------------------------------------

# 13. General Packaging Principles

-   Prefer the defined package structure whenever possible.
-   Create additional packages only when there is a genuine
    architectural need.
-   Keep responsibilities separated by layer.
-   Do not place business logic in controllers.
-   Do not place business logic in repositories.
-   Keep reusable utilities in `.util`.
-   Keep Spring configuration in `.config`.
-   Keep common external HTTP communication components in `.http`.
-   Keep exception handling centralized in `.exception`.
-   Keep database models separate from API models.
-   Prefer clear separation between incoming API objects, internal/DTO
    objects, database entities, and outgoing service objects.
