package com.example.scheme.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

// JPA Entity storing a single dynamic eligibility condition linked to a specific scheme.
@Entity
@Table(name = "scheme_rule")
public class SchemeRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheme_id", nullable = false)
    @JsonIgnore
    private Scheme scheme;

    @Column(nullable = false)
    private String field;

    @Column(nullable = false)
    private String operator;

    @Column(name = "rule_value", nullable = false)
    private String value;

    public SchemeRule() {}

    public SchemeRule(String field, String operator, String value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Scheme getScheme() { return scheme; }
    public void setScheme(Scheme scheme) { this.scheme = scheme; }
    public String getField() { return field; }
    public void setField(String field) { this.field = field; }
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}
