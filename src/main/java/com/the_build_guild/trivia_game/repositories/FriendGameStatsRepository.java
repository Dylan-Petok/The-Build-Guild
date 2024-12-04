package com.the_build_guild.trivia_game.repositories;

import com.the_build_guild.trivia_game.models.FriendGameStats;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendGameStatsRepository extends MongoRepository<FriendGameStats, String> {
    
}
