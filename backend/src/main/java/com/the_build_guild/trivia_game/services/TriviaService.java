package com.the_build_guild.trivia_game.services;

import org.springframework.stereotype.Service;

import com.the_build_guild.trivia_game.dtos.TriviaRequestDTO;
import com.the_build_guild.trivia_game.dtos.TriviaResponseDTO;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import com.the_build_guild.trivia_game.enums.TriviaStatusCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





@Service
public class TriviaService {
    
    private static final Logger logger = LoggerFactory.getLogger(TriviaService.class);


    @Value("${trivia.api.url}")
    private String triviaApiUrl;

    private final RestTemplate restTemplate;

    public TriviaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public TriviaResponseDTO getQuestions(TriviaRequestDTO triviaReqDTO) {
        String url = triviaApiUrl + "?amount=" + triviaReqDTO.getNumberOfQuestions() + "&category=" + triviaReqDTO.getCategory() + "&difficulty=" + triviaReqDTO.getDifficulty() + "&encode=url3986";
         // Log the URL
        logger.info("Making API request to URL: {}", url);
        TriviaResponseDTO response = restTemplate.getForObject(url, TriviaResponseDTO.class);
        logger.info("Response object after sending api call: {}", response);
        if (response == null) {
            throw new RuntimeException("Failed to retrieve trivia questions");
        }

        TriviaStatusCodes statusCode = TriviaStatusCodes.fromCode(response.getResponseCode());
        switch (statusCode) {
            case SUCCESS:
                return response;
            case NO_RESULTS:
            case INVALID_PARAMETER:
            case TOKEN_NOT_FOUND:
            case TOKEN_EMPTY:
            case RATE_LIMIT:
                throw new IllegalArgumentException(statusCode.getMessage());
            default:
                throw new RuntimeException("Unknown status code: " + response.getResponseCode());
        }
    }
}

