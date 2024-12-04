package com.the_build_guild.trivia_game.integration_tests.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import com.the_build_guild.trivia_game.dtos.UserCreationDTO;
import com.the_build_guild.trivia_game.dtos.UserLoginDTO;
import com.the_build_guild.trivia_game.models.User;
import com.the_build_guild.trivia_game.repositories.UserRepository;
import com.the_build_guild.trivia_game.services.UserService;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UserServiceIntegrationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void testCreateUser() {
        UserCreationDTO userCreationDTO = new UserCreationDTO();
        userCreationDTO.setUserName("testuser");
        userCreationDTO.setEmailAddr("testuser@example.com");
        userCreationDTO.setPassword("plaintext_password");

        User mockUser = new User();
        mockUser.setId("1");
        mockUser.setUsername(userCreationDTO.getUserName());
        mockUser.setEmail(userCreationDTO.getEmailAddr());
        mockUser.setPasswordHash("hashed_password");

        when(passwordEncoder.encode(userCreationDTO.getPassword())).thenReturn("hashed_password");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User createdUser = userService.createUser(userCreationDTO);

        assertNotNull(createdUser, "Created user should not be null");
        assertEquals("testuser", createdUser.getUsername());
        assertEquals("testuser@example.com", createdUser.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testUpdateScoreAndGamesPlayed() {
        String userId = "1";
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setScore(10);
        mockUser.setGamesPlayedCount(5);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.updateScoreAndGamesPlayed(userId, 5);

        assertEquals(15, mockUser.getScore(), "User score should be updated correctly");
        assertEquals(6, mockUser.getGamesPlayedCount(), "Games played count should be incremented");
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testGetGlobalRank() {
        String userId = "1";
        User mockUser1 = new User();
        mockUser1.setId(userId);
        mockUser1.setScore(100);

        User mockUser2 = new User();
        mockUser2.setId("2");
        mockUser2.setScore(150);

        when(userRepository.findAllByOrderByScoreDesc()).thenReturn(List.of(mockUser2, mockUser1));

        int rank = userService.getGlobalRank(userId);

        assertEquals(2, rank, "User rank should be correctly calculated");
    }

    @Test
    public void testAddFriend() {
        String requestingUsername = "testuser";
        String friendUsername = "frienduser";

        User requestingUser = new User();
        requestingUser.setUsername(requestingUsername);
        requestingUser.setFriends(new String[0]);

        User friendUser = new User();
        friendUser.setUsername(friendUsername);
        friendUser.setId("2");

        when(userRepository.findByUsername(requestingUsername)).thenReturn(requestingUser);
        when(userRepository.findByUsername(friendUsername)).thenReturn(friendUser);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.addFriend(requestingUsername, friendUsername);

        assertEquals(1, requestingUser.getFriends().length, "Friend should be added successfully");
        assertEquals("frienduser", requestingUser.getFriends()[0], "Friend username should match");
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testDeleteFriend() {
        String requestingUsername = "testuser";
        String friendUsername = "frienduser";

        User requestingUser = new User();
        requestingUser.setUsername(requestingUsername);
        requestingUser.setFriends(new String[]{friendUsername});

        User friendUser = new User();
        friendUser.setUsername(friendUsername);

        when(userRepository.findByUsername(requestingUsername)).thenReturn(requestingUser);
        when(userRepository.findByUsername(friendUsername)).thenReturn(friendUser);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.deleteFriend(requestingUsername, friendUsername);

        assertEquals(0, requestingUser.getFriends().length, "Friend should be removed successfully");
        verify(userRepository).save(any(User.class));
    }


    @Test
    public void testAuthenticateUser_Success() {
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUserName("testuser");
        userLoginDTO.setPassword("plaintext_password");

        User mockUser = new User();
        mockUser.setUsername("testuser");
        mockUser.setPasswordHash("hashed_password");

        when(userRepository.findByUsername(userLoginDTO.getUserName())).thenReturn(mockUser);
        when(passwordEncoder.matches(userLoginDTO.getPassword(), mockUser.getPasswordHash())).thenReturn(true);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userLoginDTO.getUserName(), userLoginDTO.getPassword());
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);

        HttpServletRequest request = org.mockito.Mockito.mock(HttpServletRequest.class);
        HttpSession session = org.mockito.Mockito.mock(HttpSession.class);
        when(request.getSession(true)).thenReturn(session);

        User authenticatedUser = userService.authenticateUser(userLoginDTO, request);

        assertNotNull(authenticatedUser, "Authenticated user should not be null");
        assertEquals("testuser", authenticatedUser.getUsername());
    }
}