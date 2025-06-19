package com.example.scheduler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ShTestApplicationTests extends AbstractTestContainerTest{

    @Test
    @DisplayName("проверка, что Postgres запущена")
    void postgresIsRunning() {
        Assertions.assertTrue(postgres.isRunning());
    }

}
