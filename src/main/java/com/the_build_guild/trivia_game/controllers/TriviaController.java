package com.the_build_guild.trivia_game.controllers;

import com.the_build_guild.trivia_game.services.GameService;
import com.the_build_guild.trivia_game.services.TriviaService;
import com.the_build_guild.trivia_game.services.UserService;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.the_build_guild.trivia_game.dtos.SaveGameDTO;
import com.the_build_guild.trivia_game.dtos.TriviaRequestDTO;
import com.the_build_guild.trivia_game.dtos.TriviaResponseDTO;
import com.the_build_guild.trivia_game.models.Game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/trivia")
public class TriviaController {

    private static final Logger logger = LoggerFactory.getLogger(TriviaController.class);

    @Autowired
    private TriviaService triviaService;

    @Autowired
    private GameService gameService;

        @Autowired
    private UserService userService;
    
    @PostMapping("/saveGame")
    public ResponseEntity<?> saveGame(@RequestBody SaveGameDTO saveGameDTO) {
        logger.info("Save game request received: {}", saveGameDTO);
        try {
            String userId = userService.getUserIdByUsername(saveGameDTO.getUserId());
            logger.info("User Id: {}", userId);
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"User not found\"}");
            }
            Game game = Game.builder()
                    .userId(userId)
                    .topic(saveGameDTO.getTopic())
                    .difficulty(saveGameDTO.getDifficulty())
                    .correctAnswers(saveGameDTO.getCorrectAnswers())
                    .totalQuestions(saveGameDTO.getTotalQuestions())
                    .gameScore(saveGameDTO.getGameScore())
                    .datePlayed(saveGameDTO.getDatePlayed())
                    .build();
            Game savedGame = gameService.saveGame(game);
            userService.updateScoreAndGamesPlayed(userId, saveGameDTO.getGameScore());
            logger.info("Game successfully saved: {}", savedGame);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedGame);
        } catch (Exception ex) {
            logger.error("Error saving game: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Error saving game\"}");
        }
    }

    @PostMapping("/play")
    public ResponseEntity<?> registerUser(@RequestBody TriviaRequestDTO triviaReqDTO){
        logger.info("Register request received for user: {}", triviaReqDTO);
        try{
            TriviaResponseDTO response = triviaService.getQuestions(triviaReqDTO);
            logger.info("Questions Successfully Sent: {}", response.getResults());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch(IllegalArgumentException ex){
            logger.info("Could not get questions for user");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\": \"" + ex.getMessage() + "\"}");
        }
        }
    
    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("Trivia API is running");
    }
    
}
