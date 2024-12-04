package com.the_build_guild.trivia_game.controllers;


import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.the_build_guild.trivia_game.dtos.AllTimeLeaderboardDTO;
import com.the_build_guild.trivia_game.dtos.PersonalBestDTO;
import com.the_build_guild.trivia_game.models.User;
import com.the_build_guild.trivia_game.repositories.GameRepository;
import com.the_build_guild.trivia_game.services.LeaderboardService;
import com.the_build_guild.trivia_game.services.UserService;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    private static final Logger logger = LoggerFactory.getLogger(LeaderboardController.class);

    @Autowired
    private LeaderboardService leaderboardService;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/allTime")
    public ResponseEntity<List<AllTimeLeaderboardDTO>> getLeaderboard() {
        logger.info("Leaderboard request received");

        // Ensure the user is authenticated
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            try {
                List<AllTimeLeaderboardDTO> leaderboard = leaderboardService.getAllTimeLeaderboard();
                return ResponseEntity.ok(leaderboard);
            } catch (Exception e) {
                logger.error("An error occurred while fetching the leaderboard", e);
                return ResponseEntity.status(500).body(null);
            }
        } else {
            return ResponseEntity.status(401).body(null);
        }
    }
    @GetMapping("/personal")
    public ResponseEntity<?> getPersonalLeaderboard(@RequestParam String username) {
        try {
            List<PersonalBestDTO> personalBestList = leaderboardService.getPersonalLeaderboardByUsername(username);
            if (personalBestList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No personal best records found.");
            }
            return ResponseEntity.ok(personalBestList);
        } catch (Exception e) {
            logger.error("Error fetching personal leaderboard: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to fetch personal leaderboard.");
        }
 
    }
    @GetMapping("/friends")
    public ResponseEntity<?> getFriendsLeaderboard() {
        logger.info("Friends Leaderboard request received");
    
        // Retrieve the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
    
        try {
            // Fetch the authenticated user
            User user = userService.findByUsername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
    
            // Get friends' IDs
            String[] friendIds = user.getFriends();
            if (friendIds == null || friendIds.length == 0) {
                return ResponseEntity.ok(Collections.emptyList());
            }
    
            // Fetch the friends' User objects
            List<User> friends = userService.getFriendsLeaderboard(friendIds);
            if (friends.isEmpty()) {
                logger.info("No friends found for user: {}", username);
                return ResponseEntity.ok(Collections.emptyList());
            }
    
            // Transform the friends into DTOs for leaderboard
            List<AllTimeLeaderboardDTO> leaderboard = friends.stream()
                .map(friend -> new AllTimeLeaderboardDTO(friend.getUsername(), friend.getScore().intValue()))
                .collect(Collectors.toList());
    
            return ResponseEntity.ok(leaderboard);
        } catch (Exception e) {
            logger.error("An error occurred while fetching the friends leaderboard", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to fetch friends leaderboard.");
        }
    }
    
    


}