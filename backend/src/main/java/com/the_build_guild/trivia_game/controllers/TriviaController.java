package com.the_build_guild.trivia_game.controllers;

import com.the_build_guild.trivia_game.services.TriviaService;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.the_build_guild.trivia_game.dtos.TriviaRequestDTO;
import com.the_build_guild.trivia_game.dtos.TriviaResponseDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/trivia")
public class TriviaController {

    private static final Logger logger = LoggerFactory.getLogger(TriviaController.class);

    @Autowired
    private TriviaService triviaService;
    
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
