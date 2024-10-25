package com.the_build_guild.trivia_game.repositories;


import com.the_build_guild.trivia_game.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String>{

}