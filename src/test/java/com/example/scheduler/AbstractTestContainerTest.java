package com.example.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.security.Principal;

/**
 * Для использования в тестах приложений, запущенных в контейнере, унаследуйтесь от данного класса
 */
public abstract class AbstractTestContainerTest {

    public final static PostgreSQLContainer postgres =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:17.5"));

    static {
        postgres.start();
    }

    @DynamicPropertySource
    static void getProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", AbstractTestContainerTest::getPostgresUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    static String getPostgresUrl() {
        return String.format("jdbc:postgresql://%s:%s/%s",
                postgres.getHost(),
                postgres.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),
                postgres.getDatabaseName()
        );
    }
}
