package com.example.scheduler.adapters.web.profile;

import com.example.scheduler.adapters.dto.CreateProfileRequest;
import com.example.scheduler.adapters.dto.ProfileResponse;
import com.example.scheduler.application.service.ProfileService;
import com.example.scheduler.domain.exception.NotEnoughAuthorityException;
import com.example.scheduler.domain.exception.ProfileAlreadyExistException;
import com.example.scheduler.domain.exception.ProfileNotFoundException;
import com.example.scheduler.domain.exception.UserNotAuthorizedException;
import com.example.scheduler.domain.model.Credential;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.example.scheduler.adapters.web.Headers.AUTH_HEADER;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

    private static final Logger log = LoggerFactory.getLogger(ProfileController.class);

    private final ProfileService service;

    public ProfileController(ProfileService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileResponse createProfile(
            @RequestBody @Valid CreateProfileRequest request,
            @RequestHeader(AUTH_HEADER) UUID userId,
            @AuthenticationPrincipal Credential credential
    ) {
        log.info("Received request to create profile for user {}", userId);
        log.debug("Create profile request = {}", request);
        ProfileResponse response = service.createProfile(userId, request, credential);
        log.info("Responded with profile created for user {}", userId);
        log.debug("Profile created = {}", response);
        return response;
    }

    @GetMapping
    public ProfileResponse getProfile(
            @RequestHeader(AUTH_HEADER) UUID userId,
            @AuthenticationPrincipal Credential credential
    ) {
        log.info("Received request for user profile: userId = {}", userId);
        ProfileResponse response = service.getProfile(userId, credential);
        log.info("Responded with requested user profile: userId = {}", userId);
        log.debug("Profile requested = {}", response);
        return response;
    }

    // TODO: Remove after [SS-40] merged
    @ExceptionHandler
    public ProblemDetail handleException(UserNotAuthorizedException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    // TODO: Remove after [SS-40] merged
    @ExceptionHandler
    public ProblemDetail handleException(NotEnoughAuthorityException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, exception.getMessage());
    }

    // TODO: Remove after [SS-40] merged
    @ExceptionHandler
    public ProblemDetail handleException(ProfileAlreadyExistException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
    }

    // TODO: Remove after [SS-40] merged
    @ExceptionHandler
    public ProblemDetail handleException(ProfileNotFoundException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }
}
