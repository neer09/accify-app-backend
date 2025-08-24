package org.accify.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "regex_patterns")
public class RegexPattern {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String pattern;

    public RegexPattern() {}

    public RegexPattern(String pattern) {
        this.pattern = pattern;
    }

    public Long getId() {
        return id;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
