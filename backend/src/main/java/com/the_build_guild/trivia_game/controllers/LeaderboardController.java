package com.the_build_guild.trivia_game.controllers;


import com.the_build_guild.trivia_game.dtos.AllTimeLeaderboardDTO;
import com.the_build_guild.trivia_game.services.LeaderboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    private static final Logger logger = LoggerFactory.getLogger(LeaderboardController.class);

    @Autowired
    private LeaderboardService leaderboardService;

    @GetMapping("/allTime")
    public ResponseEntity<List<AllTimeLeaderboardDTO>> getLeaderboard() {
        logger.info("Leaderboard request received");
        try {
            List<AllTimeLeaderboardDTO> leaderboard = leaderboardService.getAllTimeLeaderboard();
            return ResponseEntity.ok(leaderboard);
        } catch (Exception e) {
            logger.error("An error occurred while fetching the leaderboard", e);
            return ResponseEntity.status(500).body(null);
        }
    }
}