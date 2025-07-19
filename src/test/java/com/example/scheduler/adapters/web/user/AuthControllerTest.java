package com.example.scheduler.adapters.web.user;

import com.example.scheduler.adapters.fixture.TestRegisterRequests;
import com.example.scheduler.adapters.fixture.TestUserDtos;
import com.example.scheduler.application.service.AuthService;
import com.example.scheduler.application.service.UserService;
import com.example.scheduler.infrastructure.config.WithSecurityStubs;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

@WebMvcTest(AuthController.class)
@WithSecurityStubs
class AuthControllerTest {

    private static final String BASE_URL = "/auth";

    @MockitoBean
    private UserService mockUserService;

    @MockitoBean
    private AuthService mockAuthService;

    @Autowired
    private MockMvcTester mockMvcTester;

    @Test
    void givenUsernameIsNull_WhenRegister_ThenRespondWithBadRequest() {

        MvcTestResult response = mockMvcTester
                .post()
                .uri(BASE_URL + "/register")
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "password": "12345",
                          "email": "charlie@mail.com"
                        }
                        """)
                .exchange();

        then(response)
                .hasStatus(HttpStatus.BAD_REQUEST)
                .hasContentType(MediaType.APPLICATION_JSON)
                .bodyJson().isEqualTo("""
                        {
                          "timestamp": "2001-02-03T04:05:06Z",
                          "status": 400,
                          "error": "Bad Request",
                          "message": "username must not be null",
                          "path": "/auth/register"
                        }
                        """);
    }

    @Test
    void givenUsernameLongerThan255_WhenRegister_ThenRespondWithBadRequest() {
        String username = "a".repeat(256);

        MvcTestResult response = mockMvcTester
                .post()
                .uri(BASE_URL + "/register")
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "username": "%s",
                          "password": "12345",
                          "email": "charlie@mail.com"
                        }
                        """.formatted(username))
                .exchange();

        then(response)
                .hasStatus(HttpStatus.BAD_REQUEST)
                .hasContentType(MediaType.APPLICATION_JSON)
                .bodyJson().isEqualTo("""
                        {
                          "timestamp": "2001-02-03T04:05:06Z",
                          "status": 400,
                          "error": "Bad Request",
                          "message": "username size must be between 0 and 255",
                          "path": "/auth/register"
                        }
                        """);
    }

    @Test
    void givenPasswordIsNull_WhenRegister_ThenRespondWithBadRequest() {

        MvcTestResult response = mockMvcTester
                .post()
                .uri(BASE_URL + "/register")
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "username": "charlie",
                          "email": "charlie@mail.com"
                        }
                        """)
                .exchange();

        then(response)
                .hasStatus(HttpStatus.BAD_REQUEST)
                .hasContentType(MediaType.APPLICATION_JSON)
                .bodyJson().isEqualTo("""
                        {
                          "timestamp": "2001-02-03T04:05:06Z",
                          "status": 400,
                          "error": "Bad Request",
                          "message": "password must not be null",
                          "path": "/auth/register"
                        }
                        """);
    }

    @ParameterizedTest
    @ValueSource(strings = {"null", "\"\""})
    void givenEmailIsEmpty_WhenRegister_ThenRespondWithBadRequest(String email) {

        MvcTestResult response = mockMvcTester
                .post()
                .uri(BASE_URL + "/register")
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "username": "charlie",
                          "password": "12345",
                          "email": %s
                        }
                        """.formatted(email))
                .exchange();

        then(response)
                .hasStatus(HttpStatus.BAD_REQUEST)
                .hasContentType(MediaType.APPLICATION_JSON)
                .bodyJson().isEqualTo("""
                        {
                          "timestamp": "2001-02-03T04:05:06Z",
                          "status": 400,
                          "error": "Bad Request",
                          "message": "email must not be empty",
                          "path": "/auth/register"
                        }
                        """);
    }

    @Test
    void givenEmailIsMalformed_WhenRegister_ThenRespondWithBadRequest() {

        MvcTestResult response = mockMvcTester
                .post()
                .uri(BASE_URL + "/register")
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "username": "charlie",
                          "password": "12345",
                          "email": " "
                        }
                        """)
                .exchange();

        then(response)
                .hasStatus(HttpStatus.BAD_REQUEST)
                .hasContentType(MediaType.APPLICATION_JSON)
                .bodyJson().isEqualTo("""
                        {
                          "timestamp": "2001-02-03T04:05:06Z",
                          "status": 400,
                          "error": "Bad Request",
                          "message": "email must be a well-formed email address",
                          "path": "/auth/register"
                        }
                        """);
    }

    @Test
    void givenEmailIsLongerThan255_WhenRegister_ThenRespondWithBadRequest() {
        String longEmail = "a".repeat(64) + "@" + "b".repeat(63) + "." + "c".repeat(63) + "." + "d".repeat(59) + ".com";

        MvcTestResult response = mockMvcTester
                .post()
                .uri(BASE_URL + "/register")
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "username": "charlie",
                          "password": "12345",
                          "email": "%s"
                        }
                        """.formatted(longEmail))
                .exchange();

        then(response)
                .hasStatus(HttpStatus.BAD_REQUEST)
                .hasContentType(MediaType.APPLICATION_JSON)
                .bodyJson().isEqualTo("""
                        {
                          "timestamp": "2001-02-03T04:05:06Z",
                          "status": 400,
                          "error": "Bad Request",
                          "message": "email size must be between 0 and 255",
                          "path": "/auth/register"
                        }
                        """);
    }

    @Test
    void givenCorrectUsernameAndPasswordAndEmail_WhenRegister_ThenRespondWithCreatedAndUserDto() {
        given(mockUserService.registerUser(TestRegisterRequests.CHARLIE)).willReturn(TestUserDtos.CHARLIE);

        MvcTestResult response = mockMvcTester
                .post()
                .uri(BASE_URL + "/register")
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "username": "charlie",
                          "password": "12345",
                          "email": "charlie@mail.com"
                        }
                        """)
                .exchange();

        then(response)
                .hasStatus(HttpStatus.CREATED)
                .hasContentType(MediaType.APPLICATION_JSON)
                .bodyJson().isEqualTo("""
                        {
                          "id": "f089b61d-26e9-419f-9481-df5854a5312c",
                          "username": "charlie",
                          "email": "charlie@mail.com"
                        }
                        """);
    }
}
