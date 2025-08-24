package org.accify.controller;

import lombok.RequiredArgsConstructor;
import org.accify.dto.Consignment;
import org.accify.service.ErpNextService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/entry")
@RequiredArgsConstructor
public class EntryController {

    private final ErpNextService erpNextService;

    @PostMapping
    public ResponseEntity<?> addEntry(@RequestBody Consignment dto) {
        try {
            return erpNextService.createJournalEntry(dto)
                    .map(resp -> ResponseEntity.ok("✅ Sent to ERPNext\n" + resp))
                    .onErrorResume(err -> {
                        err.printStackTrace(); // 👈 Will show in logs
                        return Mono.just(ResponseEntity.internalServerError()
                                .body("❌ ERPNext error: " + err.getMessage()));
                    })
                    .block(); // blocking because controller is sync
        } catch (Exception ex) {
            ex.printStackTrace(); // 👈 Log full exception
            return ResponseEntity.internalServerError()
                    .body("❌ Internal error: " + ex.getMessage());
        }
    }

}
