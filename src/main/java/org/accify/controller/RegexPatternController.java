package org.accify.controller;

import org.accify.entity.RegexPattern;
import org.accify.service.RegexPatternService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/regex")
public class RegexPatternController {

    private final RegexPatternService service;

    public RegexPatternController(RegexPatternService service) {
        this.service = service;
    }

    // Add new regex
    @PostMapping("/add")
    public ResponseEntity<?> addPattern(@RequestBody RegexPattern regexPattern) {
        if (regexPattern.getPattern() == null || regexPattern.getPattern().isBlank()) {
            return ResponseEntity.badRequest().body("Pattern cannot be empty");
        }
        RegexPattern saved = service.savePattern(regexPattern.getPattern());
        return ResponseEntity.ok(saved);
    }

    // Fetch all regex
    @GetMapping("/list")
    public List<RegexPattern> getAllPatterns() {
        return service.getAllPatterns();
    }

    // Delete regex by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePattern(@PathVariable Long id) {
        service.deletePattern(id);
        return ResponseEntity.ok("Deleted pattern with ID " + id);
    }
}
