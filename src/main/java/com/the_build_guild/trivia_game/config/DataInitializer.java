package com.the_build_guild.trivia_game.config;


import com.the_build_guild.trivia_game.models.*;
import com.the_build_guild.trivia_game.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.logging.Logger;

//class is meant to initialize the collections and add some dummy data into it


@Configuration
public class DataInitializer {

        private static final Logger logger = Logger.getLogger(DataInitializer.class.getName());


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;


    
    @Autowired
    private FriendGameStatsRepository friendGameStatsRepository;

    @Value("${data.initializer.enabled}")
    private boolean isDataInitializerEnabled;

    @Bean
    public CommandLineRunner loadData() {
        return args -> {
         if (isDataInitializerEnabled) {

                // Clear existing data
            userRepository.deleteAll();
            gameRepository.deleteAll();
            friendGameStatsRepository.deleteAll();


            // Create dummy users
            User user1 = User.builder()
                    .username("john_doe")
                    .email("john@example.com")
                    .passwordHash("hashed_password")
                    .friends(new String[]{"friend1", "friend2"})
                    .build();

            User user2 = User.builder()
                    .username("jane_doe")
                    .email("jane@example.com")
                    .passwordHash("hashed_password2")
                    .friends(new String[]{"friend3", "friend4"})
                    .build();

            userRepository.saveAll(Arrays.asList(user1, user2));
            logger.info("Users saved: " + userRepository.findAll());

       
            // Create dummy games
            Game game1 = Game.builder()
                        .userId(user1.getId())
                        .topic("Science")
                        .difficulty("Medium")
                        .correctAnswers(8)
                        .totalQuestions(10)
                        .gameScore(80)
                        .datePlayed(new Date())
                        .build();

                Game game2 = Game.builder()
                        .userId(user1.getId())
                        .topic("History")
                        .difficulty("Hard")
                        .correctAnswers(5)
                        .totalQuestions(10)
                        .gameScore(50)
                        .datePlayed(new Date())
                        .build();

                Game game3 = Game.builder()
                        .userId(user2.getId())
                        .topic("History")
                        .difficulty("Medium")
                        .correctAnswers(7)
                        .totalQuestions(10)
                        .gameScore(70)
                        .datePlayed(new Date())
                        .build();

            gameRepository.saveAll(Arrays.asList(game1, game2, game3));
            logger.info("Games saved: " + gameRepository.findAll());

              // Create dummy friend game stats
              FriendGameStats stats1 = FriendGameStats.builder()
              .friendShipId("friendship1")
              .userId1(user1.getId())
              .userId2(user2.getId())
              .winCountUser1(3)
              .winCountUser2(2)
              .build();

      FriendGameStats stats2 = FriendGameStats.builder()
              .friendShipId("friendship2")
              .userId1(user2.getId())
              .userId2(user1.getId())
              .winCountUser1(1)
              .winCountUser2(4)
              .build();

      friendGameStatsRepository.saveAll(Arrays.asList(stats1, stats2));
      logger.info("Friend game stats saved: " + friendGameStatsRepository.findAll());
         }else {
                logger.info("Data initialization is disabled.");

         }
        };
    }
}