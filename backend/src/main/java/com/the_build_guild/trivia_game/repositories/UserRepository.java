package com.the_build_guild.trivia_game.repositories;


import com.the_build_guild.trivia_game.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{

}