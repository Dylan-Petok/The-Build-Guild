package com.the_build_guild.trivia_game.services;

import com.the_build_guild.trivia_game.models.User;
import com.the_build_guild.trivia_game.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
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
    }