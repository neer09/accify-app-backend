package org.accify.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health-check")
@CrossOrigin
public class HealthCheckController {

    private static final Logger log = LoggerFactory.getLogger(HealthCheckController.class);
    
    @GetMapping
    public ResponseEntity<String> healthCheck() {
        log.info("Accify is up & running!");
        return ResponseEntity.ok("Accify is up & running!");
    }
}
