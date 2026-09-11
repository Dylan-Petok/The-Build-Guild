package com.the_build_guild.trivia_game.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import com.the_build_guild.trivia_game.models.User;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.the_build_guild.trivia_game.services.GameService;
import com.the_build_guild.trivia_game.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

import com.the_build_guild.trivia_game.dtos.UserLoginDTO;
import com.the_build_guild.trivia_game.dtos.UserCreationDTO;


@WebMvcTest(UserController.class)


@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    MockMvc fakeRequestMaker;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @MockBean
    GameService gameService;

    @Test
    void login_ReturnsUnauthorized_WhenPasswordIsInvalid() throws Exception {

        UserLoginDTO userLoginDTO = new UserLoginDTO("test1", "password1");

        String requestBody = objectMapper.writeValueAsString(userLoginDTO);


        when(userService.authenticateUser(any(UserLoginDTO.class), any(HttpServletRequest.class)))
                .thenThrow(new BadCredentialsException("Invalid password"));

        fakeRequestMaker.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid password"));
    }

    @Test
    void login_ReturnsSuccess_WhenValid() throws Exception {
        
        UserLoginDTO userLoginDTO = new UserLoginDTO("test2", "password2");

        String requestBody = objectMapper.writeValueAsString(userLoginDTO);

        User authenticatedUser = User.builder()
            .username("test2")
            .friends(new String[]{"friendA", "friendB"})
            .build();

        when(userService.authenticateUser(any(UserLoginDTO.class), any(HttpServletRequest.class)))
            .thenReturn(authenticatedUser);

        fakeRequestMaker.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Login successful"))
        .andExpect(jsonPath("$.username").value("test2"))
        .andExpect(jsonPath("$.friends.length()").value(2))
        .andExpect(jsonPath("$.friends[0]").value("friendA"))
        .andExpect(jsonPath("$.friends[1]").value("friendB"));
    }

    @Test 
    void createAccount_ReturnsBadRequest_WhenInputInvalid() throws Exception {
        UserCreationDTO dto = new UserCreationDTO("test3", "test3@example.com", "password3");

        String requestBody = objectMapper.writeValueAsString(dto);

        when(userService.createUser(any(UserCreationDTO.class)))
            .thenThrow(new IllegalArgumentException("Username already taken"));

        fakeRequestMaker.perform(post("/api/users/create")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Username already taken"));
    }
}
