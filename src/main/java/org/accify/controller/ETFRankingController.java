package org.accify.controller;

import org.accify.dto.ETFRank;
import org.accify.service.ETFRankingService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/etfs")
@CrossOrigin
public class ETFRankingController {

    private final ETFRankingService rankingService;

    public ETFRankingController(ETFRankingService rankingService) {
        this.rankingService = rankingService;
    }

    @GetMapping("/top-10/20-dma")
    public List<ETFRank> top10() {
        return rankingService.getTop10By20DMA();
    }
}

