package org.accify.controller;

import org.accify.entity.ETFWatchlist;
import org.accify.repo.ETFWatchlistRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/watchlist")
public class ETFWatchlistController {

    private final ETFWatchlistRepository repo;

    public ETFWatchlistController(ETFWatchlistRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<ETFWatchlist> getAll() {
        return repo.findAll();
    }

    @PostMapping("/add")
    public String add(@RequestParam String symbol) {
        if (repo.existsBySymbol(symbol)) return "Already exists";
        ETFWatchlist entry = new ETFWatchlist();
        entry.setSymbol(symbol);
        repo.save(entry);
        return "Added " + symbol;
    }

    @DeleteMapping("/remove")
    public String remove(@RequestParam String symbol) {
        repo.findBySymbol(symbol).ifPresent(repo::delete);
        return "Removed " + symbol;
    }
}

