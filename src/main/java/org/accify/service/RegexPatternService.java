package org.accify.service;

import org.accify.entity.RegexPattern;
import org.accify.repo.RegexPatternRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegexPatternService {

    private final RegexPatternRepository repository;

    public RegexPatternService(RegexPatternRepository repository) {
        this.repository = repository;
    }

    public RegexPattern savePattern(String pattern) {
        RegexPattern regexPattern = new RegexPattern(pattern);
        return repository.save(regexPattern);
    }

    public List<RegexPattern> getAllPatterns() {
        return repository.findAll();
    }

    public void deletePattern(Long id) {
        repository.deleteById(id);
    }
}
