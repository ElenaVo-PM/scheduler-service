package com.example.scheduler.adapters.web.profile;

import com.example.scheduler.adapters.dto.ProfilePublicDto;
import com.example.scheduler.application.service.ProfileService;
import com.example.scheduler.domain.exception.ProfileNotFoundException;
import com.example.scheduler.infrastructure.config.WithSecurityStubs;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import java.util.UUID;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

@WebMvcTest(PublicProfileController.class)
@WithSecurityStubs
class PublicProfileControllerTest {

    private static final String BASE_URL = "/api/v1/public/profiles";

    @MockitoBean
    private ProfileService mockProfileService;

    @Autowired
    private MockMvcTester mockMvcTester;

    @Test
    void givenProfileExists_WhenGetPublicProfile_ThenRespondWithProfileData() {
        UUID userId = UUID.fromString("d3e68c3b-2d6d-48a1-a037-99a390e9433e");
        ProfilePublicDto expectedResponse = new ProfilePublicDto("Alice Arno", "logo.jpg");

        given(mockProfileService.getPublicProfile(userId))
                .willReturn(expectedResponse);

        MvcTestResult response = mockMvcTester
                .get()
                .uri(BASE_URL + "/" + userId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        then(response)
                .hasStatus(HttpStatus.OK)
                .hasContentType(MediaType.APPLICATION_JSON)
                .bodyJson().isEqualTo("""
                        {
                            "fullName": "Alice Arno",
                            "logo": "logo.jpg"
                        }
                        """);
    }

    @Test
    void givenProfileNotExists_WhenGetPublicProfile_ThenRespondWithNotFound() {
        UUID userId = UUID.randomUUID();

        given(mockProfileService.getPublicProfile(userId))
                .willThrow(new ProfileNotFoundException("Profile not found"));

        MvcTestResult response = mockMvcTester
                .get()
                .uri(BASE_URL + "/" + userId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        then(response)
                .hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    void givenInvalidProfileId_WhenGetPublicProfile_ThenRespondWithBadRequest() {
        MvcTestResult response = mockMvcTester
                .get()
                .uri(BASE_URL + "/invalid-uuid")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        then(response)
                .hasStatus(HttpStatus.BAD_REQUEST);
    }
}
