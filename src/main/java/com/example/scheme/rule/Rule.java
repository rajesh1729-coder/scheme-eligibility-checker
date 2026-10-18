package com.example.scheme.rule;

import com.example.scheme.dto.UserProfile;

// Strategy interface representing a single evaluable eligibility criterion against a UserProfile.
public interface Rule {
    boolean isSatisfiedBy(UserProfile user);
}
