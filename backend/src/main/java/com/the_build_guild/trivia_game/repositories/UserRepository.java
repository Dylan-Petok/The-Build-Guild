package com.the_build_guild.trivia_game.repositories;


import com.the_build_guild.trivia_game.models.User;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String>{
    // Custom query method to find a user by username
    User findByUsername(String username);

    List<User> findAllByOrderByScoreDesc();

    List<User> findByUsernameContainingIgnoreCase(String username);
    
    List<User> findAllByIdIn(List<String> friendsIds);

    User findByEmail(String email);

}