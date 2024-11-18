package com.the_build_guild.trivia_game.controllers;

import com.the_build_guild.trivia_game.models.User;
import com.the_build_guild.trivia_game.services.UserService;
import com.the_build_guild.trivia_game.services.GameService;
import com.the_build_guild.trivia_game.dtos.ProfileInfoDTO;
import com.the_build_guild.trivia_game.dtos.UserCreationDTO;
import com.the_build_guild.trivia_game.dtos.UserLoginDTO;



import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.catalina.connector.Response;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

@RequestMapping("/api/users")
@RestController
public class UserController {
    
     private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    private final UserService userService;

    private final GameService gameService;



    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody UserCreationDTO userCreationDTO) {
        try{
            User user = userService.createUser(userCreationDTO);
            if (user != null) {
                String token = userService.generateToken(user);
                setAuthentication(user);
                return ResponseEntity.ok("{\"token\": \"" + token + "\"}");
            } else {
                return ResponseEntity.status(400).body("{\"message\": \"Failed to login account\"}");
            }
        }catch(Exception e) {
            return ResponseEntity.status(500).body("{\"message\": \"An error occurred while logging in account\"}");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO userLoginDTO) {
        logger.info("Login attempt for user: {}", userLoginDTO);
        try{
            User user = userService.authenticateUser(userLoginDTO);
            if (user != null) {
                String token = userService.generateToken(user);
                setAuthentication(user);

                  // Create a response map
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                response.put("username", user.getUsername());

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(400).body("{\"message\": \"Failed to login account\"}");
            }
        }catch(Exception e) {
            return ResponseEntity.status(500).body("{\"message\": \"An error occurred while logging in account\"}");
        }
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

    
    private void setAuthentication(User user) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPasswordHash())
                .roles("USER")
                .build();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}