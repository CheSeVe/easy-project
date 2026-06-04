package ru.cheseve.easyproject;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public interface TestContainersConfig {
    @Container
    @ServiceConnection
    PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");
}
