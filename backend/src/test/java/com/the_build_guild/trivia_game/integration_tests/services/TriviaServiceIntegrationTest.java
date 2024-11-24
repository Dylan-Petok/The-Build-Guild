package com.the_build_guild.trivia_game.integration_tests.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import com.the_build_guild.trivia_game.dtos.TriviaRequestDTO;
import com.the_build_guild.trivia_game.dtos.TriviaResponseDTO;
import com.the_build_guild.trivia_game.enums.TriviaStatusCodes;
import com.the_build_guild.trivia_game.services.TriviaService;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest
class TriviaServiceIntegrationTest {

	private static final Logger logger = LoggerFactory.getLogger(TriviaServiceIntegrationTest.class);

	@Mock
	private RestTemplate restTemplate;
	
	@InjectMocks
	private TriviaService triviaService;
	

	@Test
	public void testGetQuestions_Success(){

		TriviaRequestDTO request = new TriviaRequestDTO();
		request.setNumberOfQuestions("5");
		request.setCategory("Science");
		request.setDifficulty("easy");

		TriviaResponseDTO mockResponse = new TriviaResponseDTO();
		mockResponse.setResponseCode(TriviaStatusCodes.SUCCESS.getCode());

		when(restTemplate.getForObject(any(String.class), any(Class.class)))
			.thenReturn(mockResponse);

		TriviaResponseDTO response = triviaService.getQuestions(request);

		assertNotNull(response, "The response should not be null");
		assertNotNull(response.getResponseCode(), "The response code should not be null");
	}

	@Test
    public void testGetQuestions_NoResults() {

        TriviaRequestDTO request = new TriviaRequestDTO();
        request.setNumberOfQuestions("5");
        request.setCategory("Science");
        request.setDifficulty("easy");

        TriviaResponseDTO mockResponse = new TriviaResponseDTO();
        mockResponse.setResponseCode(TriviaStatusCodes.NO_RESULTS.getCode());

        when(restTemplate.getForObject(any(String.class), any(Class.class))).thenReturn(mockResponse);

        assertThrows(IllegalArgumentException.class, () -> {
            triviaService.getQuestions(request);
        });
    }

    @Test
    public void testGetQuestions_InvalidParameter() {

        TriviaRequestDTO request = new TriviaRequestDTO();
        request.setNumberOfQuestions("5");
        request.setCategory("InvalidCategory");

        TriviaResponseDTO mockResponse = new TriviaResponseDTO();
        mockResponse.setResponseCode(TriviaStatusCodes.INVALID_PARAMETER.getCode());

        when(restTemplate.getForObject(any(String.class), any(Class.class))).thenReturn(mockResponse);

        assertThrows(IllegalArgumentException.class, () -> {
            triviaService.getQuestions(request);
        });
    }

    @Test
    public void testGetQuestions_UnknownStatusCode() {

        TriviaRequestDTO request = new TriviaRequestDTO();
        request.setNumberOfQuestions("5");
        request.setCategory("Science");
        request.setDifficulty("easy");

        TriviaResponseDTO mockResponse = new TriviaResponseDTO();
        mockResponse.setResponseCode(999);

        when(restTemplate.getForObject(any(String.class), any(Class.class))).thenReturn(mockResponse);

        assertThrows(RuntimeException.class, () -> {
            triviaService.getQuestions(request);
        });
    }

	// Kind of a weird way to set the triviaApiUrl, but I figured I would use reflection so I didn't have to go in and change the TriviaService to contain an extra apiUrl variable.
    @Test
    public void testGetQuestions_InvalidApiUrl() {

		try {
			Field field = TriviaService.class.getDeclaredField("triviaApiUrl");
			field.setAccessible(true);
			field.set(triviaService, "invalid-url");
		} catch (NoSuchFieldException e) {
            logger.error("Field 'triviaApiUrl' not found in TriviaService class", e);
        } catch (NullPointerException e) {
            logger.error("TriviaService instance is null", e);
        } catch (SecurityException e) {
            logger.error("Security exception occurred while accessing 'triviaApiUrl'", e);
        } catch (IllegalAccessException e) {
            logger.error("Illegal access when setting 'triviaApiUrl' field", e);
        }


        TriviaRequestDTO request = new TriviaRequestDTO();
        request.setNumberOfQuestions("5");
        request.setCategory("Science");
        request.setDifficulty("easy");

        assertThrows(RuntimeException.class, () -> {
            triviaService.getQuestions(request);
        });
    }

}