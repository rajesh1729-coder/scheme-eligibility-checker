package com.example.scheme.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

// JPA Entity representing a government welfare scheme and its associated eligibility rules.
@Entity
@Table(name = "scheme")
public class Scheme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    private String benefit;
    private String applyLink;
    private String sourceUrl;

    @OneToMany(mappedBy = "scheme", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<SchemeRule> rules = new ArrayList<>();

    public Scheme() {}

    public Scheme(Long id, String name, String description, String benefit, String applyLink, String sourceUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.benefit = benefit;
        this.applyLink = applyLink;
        this.sourceUrl = sourceUrl;
    }

    public void addRule(SchemeRule rule) {
        rules.add(rule);
        rule.setScheme(this);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getBenefit() { return benefit; }
    public void setBenefit(String benefit) { this.benefit = benefit; }
    public String getApplyLink() { return applyLink; }
    public void setApplyLink(String applyLink) { this.applyLink = applyLink; }
    public String getSourceUrl() { return sourceUrl; }
    public void setSourceUrl(String sourceUrl) { this.sourceUrl = sourceUrl; }
    public List<SchemeRule> getRules() { return rules; }
    public void setRules(List<SchemeRule> rules) { this.rules = rules; }
}
