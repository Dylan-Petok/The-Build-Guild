package com.the_build_guild.trivia_game.controllers;

import com.the_build_guild.trivia_game.models.User;
import com.the_build_guild.trivia_game.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import com.the_build_guild.trivia_game.services.GameService;
import com.the_build_guild.trivia_game.dtos.ProfileInfoDTO;
import com.the_build_guild.trivia_game.dtos.UserCreationDTO;
import com.the_build_guild.trivia_game.dtos.UserLoginDTO;



import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.catalina.connector.Response;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.List;

@RequestMapping("/api/users")
@RestController
public class UserController {
    
     private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    private final UserService userService;

    private final GameService gameService;



    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody UserCreationDTO userCreationDTO, HttpServletRequest request) {
        try {
            User user = userService.createUser(userCreationDTO);

            UserLoginDTO userLoginDTO = new UserLoginDTO();
            userLoginDTO.setUserName(user.getUsername());
            userLoginDTO.setPassword(userCreationDTO.getPassword());
            userService.authenticateUser(userLoginDTO, request);
            return ResponseEntity.ok(Map.of("message", "Account created successfully", "username", user.getUsername()));
        } catch (IllegalArgumentException e) {
            logger.error("Error creating account: {}", e.getMessage());
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error creating account", e);
            return ResponseEntity.status(500).body(Map.of("message", "An error occurred while creating the account"));
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request) {
        logger.info("Login attempt for user: {}", userLoginDTO);
        try {
            User user = userService.authenticateUser(userLoginDTO, request); // Handles authentication and SecurityContext
             HttpSession session = request.getSession(true); // Ensure session is created
            logger.info("Session created for user: {}, Session ID: {}", user.getUsername(), session.getId());
            return ResponseEntity.ok(Map.of("message", "Login successful", "username", user.getUsername(), "friends", user.getFriends()));
        } catch (UsernameNotFoundException e) {
            logger.error("Error during login: User not found", e);
            return ResponseEntity.status(404).body(Map.of("message", "User not found"));
        } catch (BadCredentialsException e) {
            logger.error("Error during login: Invalid password", e);
            return ResponseEntity.status(401).body(Map.of("message", "Invalid password"));
        } catch (Exception e) {
            logger.error("Error during login", e);
            return ResponseEntity.status(500).body(Map.of("message", "Login failed"));
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }


    
    @GetMapping("/profileInfo")
    public ResponseEntity<?> profileInfo(){
        try{
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = userDetails.getUsername();
            User user = userService.findByUsername(username);
            int globalRank = userService.getGlobalRank(user.getId());
            int highestScoreInGame = gameService.getHighestScoreInGame(user.getId());
            int currentScore = user.getScore();
            int gamesPlayedCount = user.getGamesPlayedCount();
            ProfileInfoDTO profileInfo = ProfileInfoDTO.builder()
                .gamesPlayedCount(gamesPlayedCount)
                .highestScoreInGame(highestScoreInGame)
                .globalRank(globalRank)
                .currentScore(currentScore)
                .build();
            return ResponseEntity.ok(profileInfo);
        }catch(Exception e){
            return ResponseEntity.status(500).body("An error occured while fetching profile information");
        }
    }
    
    @GetMapping("/search")
public ResponseEntity<?> searchUsers(@RequestParam String query) {
    try {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            String requestingUsername = userDetails.getUsername();
            logger.info("Search request made by user: {}", requestingUsername); // Log the username
            List<User> users = userService.searchUsers(query, requestingUsername);
            if (users.isEmpty()) {
                return ResponseEntity.status(404).body("No users found with the given query");
            }
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.status(401).body("Unauthorized");
        }
    } catch (Exception e) {
        return ResponseEntity.status(500).body("An error occurred while searching for users");
    }
}

@PostMapping("/addFriend")
public ResponseEntity<?> addFriend(@RequestBody Map<String, String> requestBody) {
    String friendUsername = requestBody.get("username");
    logger.info("Add friend request received for friendUsername: {}", friendUsername);
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof UserDetails) {
        UserDetails userDetails = (UserDetails) principal;
        String requestingUsername = userDetails.getUsername();
        logger.info("User Requesting friend: {}", requestingUsername);
        try {
            userService.addFriend(requestingUsername, friendUsername);
            User user = userService.findByUsername(requestingUsername);
            return ResponseEntity.ok(Map.of("message", "Friend added successfully", "friendsList", user.getFriends()));
        } catch (Exception e) {
            logger.error("Error adding friend", e);
            return ResponseEntity.status(500).body(Map.of("message", "An error occurred while adding the friend"));
        }
    } else {
        return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
    }
}


@PostMapping("/deleteFriend")
public ResponseEntity<?> deleteFriend(@RequestBody Map<String, String> requestBody) {
    String friendUsername = requestBody.get("username");
    logger.info("Delete friend request received for friendUsername: {}", friendUsername);
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof UserDetails) {
        UserDetails userDetails = (UserDetails) principal;
        String requestingUsername = userDetails.getUsername();
        logger.info("User requesting delete: {}", requestingUsername);
        try {
            userService.deleteFriend(requestingUsername, friendUsername);
            User user = userService.findByUsername(requestingUsername);
            return ResponseEntity.ok(Map.of("message", "Friend deleted successfully", "friendsList", user.getFriends()));
        } catch (Exception e) {
            logger.error("Error deleting friend", e);
            return ResponseEntity.status(500).body(Map.of("message", "An error occurred while deleting the friend"));
        }
    } else {
        return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
    }
}

    

    @Autowired
    public UserController(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;

    }

    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/test")
    public ResponseEntity<?> testEndpoint() {
        logger.info("Test endpoint accessed");
        return ResponseEntity.ok("Test endpoint is accessible");
    }
    @GetMapping("/leaderboard/friends")
    public ResponseEntity<?> getFriendsLeaderboard(HttpServletRequest request) {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                String username = userDetails.getUsername();
                User user = userService.findByUsername(username);
    
                List<User> friends = userService.getFriendsLeaderboard(user.getFriends());
                List<FriendsDTO> leaderboard = friends.stream()
                    .map(friend -> new FriendsDTO(friend.getUsername(), friend.getScore().intValue())) 
                    .collect(Collectors.toList());
                return ResponseEntity.ok(leaderboard);
            } else {
                return ResponseEntity.status(401).body("Unauthorized");
            }
        } catch (Exception e) {
            logger.error("Error fetching friends leaderboard", e);
            return ResponseEntity.status(500).body("An error occurred while fetching the leaderboard");
        }
    }
    


    

}