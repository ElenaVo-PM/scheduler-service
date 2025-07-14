package com.example.scheduler.adapters.web.rule;

import com.example.scheduler.adapters.dto.AvailabilityRuleResponse;
import com.example.scheduler.adapters.dto.CreateAvailabilityRuleRequest;
import com.example.scheduler.application.service.AvailabilityRuleService;
import com.example.scheduler.domain.model.Credential;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/availability-rules")
public class AvailabilityRuleController {

    private final AvailabilityRuleService ruleService;

    public AvailabilityRuleController(AvailabilityRuleService ruleService) {
        this.ruleService = ruleService;
    }

    @PostMapping
    public ResponseEntity<AvailabilityRuleResponse> createRule(@RequestBody
                                                               @Valid
                                                               CreateAvailabilityRuleRequest request,
                                                               @AuthenticationPrincipal
                                                               Credential credential) {
        AvailabilityRuleResponse rule = ruleService.createRule(request, credential);
        return ResponseEntity.status(HttpStatus.CREATED).body(rule);
    }

}
