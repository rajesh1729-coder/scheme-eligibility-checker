package com.example.scheme.service;

import com.example.scheme.dto.SchemeRequest;
import com.example.scheme.dto.UserProfile;
import com.example.scheme.model.Scheme;
import com.example.scheme.model.SchemeRule;
import com.example.scheme.repository.SchemeRepository;
import com.example.scheme.rule.RuleFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

// Business logic layer that manages schemes and evaluates whether a citizen satisfies all rules of each scheme.
@Service
public class SchemeService {
    private final SchemeRepository schemeRepository;
    private final RuleFactory ruleFactory;

    public SchemeService(SchemeRepository schemeRepository, RuleFactory ruleFactory) {
        this.schemeRepository = schemeRepository;
        this.ruleFactory = ruleFactory;
    }

    public List<Scheme> getAllSchemes() {
        return schemeRepository.findAll();
    }

    public Scheme getSchemeById(Long id) {
        return schemeRepository.findById(id)
                .orElseThrow(() -> new com.example.scheme.exception.ResourceNotFoundException("Scheme not found with id: " + id));
    }

    @Transactional
    public void deleteScheme(Long id) {
        Scheme scheme = getSchemeById(id);
        schemeRepository.delete(scheme);
    }

    @Transactional
    public Scheme createScheme(SchemeRequest request) {
        Scheme scheme = new Scheme(null, request.name(), request.description(),
                request.benefit(), request.applyLink(), request.sourceUrl());
        request.rules().forEach(r -> {
            SchemeRule ruleEntity = new SchemeRule(r.field(), r.operator(), r.value());
            ruleFactory.createRule(ruleEntity); // Fail-fast validation of rule syntax
            scheme.addRule(ruleEntity);
        });
        return schemeRepository.save(scheme);
    }

    public List<Scheme> checkEligibility(UserProfile user) {
        return schemeRepository.findAll().stream()
                .filter(scheme -> isEligibleForScheme(scheme, user))
                .toList();
    }

    private boolean isEligibleForScheme(Scheme scheme, UserProfile user) {
        return scheme.getRules().stream()
                .map(ruleFactory::createRule)
                .allMatch(rule -> rule.isSatisfiedBy(user));
    }
}
