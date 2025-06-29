package com.example.scheduler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShTestApplicationTests {

    @Test
    @DisplayName("проверка, что Postgres запущена")
    void whenLoadContext_ThenNoErrors() {
        Assertions.assertTrue(true);
    }
}
