package com.example.scheduler.adapters.web.user;

import com.example.scheduler.AbstractTestContainerTest;
import com.example.scheduler.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class AuthControllerTest extends AbstractTestContainerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository repo;

    @Test
    void shouldAuthenticateValid() throws Exception {

        String username = "username";
        String password = "password";
        String encodedPassword = encoder.encode(password);
        String email = "email@mail.com";

        repo.save(username, encodedPassword, email);

        mvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "username": "username",
                            "password": "password"
                        }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    void shouldSendUnauthorized() throws Exception {
        mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "user",
                            "password": "pass"
                        }
                        """))
                .andExpect(status().isUnauthorized());
    }

}
