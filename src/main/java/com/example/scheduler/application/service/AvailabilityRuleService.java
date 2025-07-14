package com.example.scheduler.application.service;

import com.example.scheduler.adapters.dto.AvailabilityRuleResponse;
import com.example.scheduler.adapters.dto.CreateAvailabilityRuleRequest;
import com.example.scheduler.domain.exception.DataConflictException;
import com.example.scheduler.domain.model.AvailabilityRule;
import com.example.scheduler.domain.model.Credential;
import com.example.scheduler.domain.repository.AvailabilityRuleRepository;
import com.example.scheduler.infrastructure.mapper.AvailabilityRuleMapper;
import org.springframework.stereotype.Service;

@Service
public class AvailabilityRuleService {

    private final AvailabilityRuleRepository ruleRepository;
    private final AvailabilityRuleMapper ruleMapper;

    public AvailabilityRuleService(AvailabilityRuleRepository ruleRepository, AvailabilityRuleMapper ruleMapper) {
        this.ruleRepository = ruleRepository;
        this.ruleMapper = ruleMapper;
    }

    public AvailabilityRuleResponse createRule(CreateAvailabilityRuleRequest request, Credential credential) {
        AvailabilityRule newRule = ruleMapper.toEntity(request, credential.getId());
        if (ruleRepository.intersects(newRule)) {
            throw new DataConflictException("The rule has time intersection with another saved rule");
        }
        AvailabilityRule savedRule = ruleRepository.save(newRule);
        return ruleMapper.toDto(savedRule);
    }
}
