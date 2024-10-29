package com.the_build_guild.trivia_game.services;

import com.the_build_guild.trivia_game.dtos.UserCreationDTO;
import com.the_build_guild.trivia_game.dtos.UserLoginDTO;
import com.the_build_guild.trivia_game.models.User;
import com.the_build_guild.trivia_game.repositories.UserRepository;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class UserService {
        private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateUser(String id, User userDetails) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            user.setPasswordHash(userDetails.getPasswordHash());
            user.setFriends(userDetails.getFriends());
            user.setGamesPlayed(userDetails.getGamesPlayed());
            user.setWinCount(userDetails.getWinCount());
            return userRepository.save(user);
        }
        return null;
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

     public User createUser(UserCreationDTO userCreationDTO) {
        User user = new User();
        user.setUsername(userCreationDTO.getUserName());
        user.setEmail(userCreationDTO.getEmailAddr());
        user.setPasswordHash(userCreationDTO.getPassword()); // Ensure to hash the password before saving
        return userRepository.save(user);
    }
    
    public User authenticateUser(UserLoginDTO userLoginDTO) {
        logger.info("Authenticating user: {}", userLoginDTO.getUserName());
        User user = userRepository.findByUsername(userLoginDTO.getUserName());
        if (user != null && user.getPasswordHash().equals(userLoginDTO.getPassword())) {
            logger.info("Authentication successful for user: {}", userLoginDTO.getUserName());
            return user;
        }
        logger.warn("Authentication failed for user: {}", userLoginDTO.getUserName());
        return null;
    }
    
    }