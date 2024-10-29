package com.the_build_guild.trivia_game.controllers;

import com.the_build_guild.trivia_game.models.User;
import com.the_build_guild.trivia_game.services.UserService;
import com.the_build_guild.trivia_game.dtos.UserCreationDTO;
import com.the_build_guild.trivia_game.dtos.UserLoginDTO;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

@RequestMapping("/api/users")
@RestController
public class UserController {
    
    private final UserService userService;



    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody UserCreationDTO userCreationDTO) {
        try{
            User user = userService.createUser(userCreationDTO);
            if (user != null) {
                return ResponseEntity.ok("Account created successfully");
            } else {
                return ResponseEntity.status(400).body("Failed to create account");
            }
        }catch(Exception e) {
            return ResponseEntity.status(500).body("An error occured while creating account");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO userLoginDTO) {
        try{
            User user = userService.authenticateUser(userLoginDTO);
            if (user != null) {
                return ResponseEntity.ok("Account logged in successfully");
            } else {
                return ResponseEntity.status(400).body("Failed to login account");
            }
        }catch(Exception e) {
            return ResponseEntity.status(500).body("An error occured while loggin in account");
        }
    }
    
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody User user){
        userService.createUser(user);
        return ResponseEntity.ok("Successfully created user");
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
}