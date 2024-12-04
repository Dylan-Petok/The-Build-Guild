package com.the_build_guild.trivia_game.repositories;

import com.the_build_guild.trivia_game.models.Game;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface GameRepository extends MongoRepository<Game, String> {
    List<Game> findByUserIdContaining(String userId);
}
