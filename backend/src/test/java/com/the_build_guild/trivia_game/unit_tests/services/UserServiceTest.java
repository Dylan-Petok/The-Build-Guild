package com.the_build_guild.trivia_game.unit_tests.services;

import com.the_build_guild.trivia_game.dtos.UserCreationDTO;
import com.the_build_guild.trivia_game.dtos.UserLoginDTO;
import com.the_build_guild.trivia_game.models.User;
import com.the_build_guild.trivia_game.repositories.UserRepository;
import com.the_build_guild.trivia_game.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserService userService;

    @Test
    public void testCreateUser() {
        UserCreationDTO userCreationDTO = new UserCreationDTO("user1", "user1@example.com", "password123");
        User user = User.builder().username("user1").email("user1@example.com").passwordHash("hashed_password").build();
        when(passwordEncoder.encode("password123")).thenReturn("hashed_password");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(userCreationDTO);

        assertNotNull(createdUser);
        assertEquals("user1", createdUser.getUsername());
        assertEquals("user1@example.com", createdUser.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testGetUserById_UserExists() {
        String userId = "1";
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setUsername("testuser");

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        User foundUser = userService.getUserById(userId);

        assertNotNull(foundUser, "Found user should not be null");
        assertEquals("testuser", foundUser.getUsername());
    }

    @Test
    public void testGetUserById_UserNotExists() {
        String userId = "2";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        User foundUser = userService.getUserById(userId);

        assertNull(foundUser, "User should be null when not found");
    }

    @Test
    public void testDeleteUser_UserExists() {
        String userId = "1";
        doNothing().when(userRepository).deleteById(userId);

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testDeleteUser_UserNotExists() {
        String userId = "2";
        doNothing().when(userRepository).deleteById(userId);

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testFindByUsername_UserExists() {
        String username = "testuser";
        User mockUser = new User();
        mockUser.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(mockUser);

        User foundUser = userService.findByUsername(username);

        assertNotNull(foundUser, "Found user should not be null");
        assertEquals(username, foundUser.getUsername());
    }

    @Test
    public void testFindByUsername_UserNotExists() {
        String username = "nonExistentUser";
        when(userRepository.findByUsername(username)).thenReturn(null);

        User foundUser = userService.findByUsername(username);

        assertNull(foundUser, "User should be null when not found");
    }

    @Test
    public void testGetUserIdByUsername_UserExists() {
        String username = "testuser";
        User mockUser = new User();
        mockUser.setId("1");
        mockUser.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(mockUser);

        String userId = userService.getUserIdByUsername(username);

        assertEquals("1", userId, "User ID should match");
    }

    @Test
    public void testGetUserIdByUsername_UserNotExists() {
        String username = "nonExistentUser";
        when(userRepository.findByUsername(username)).thenReturn(null);

        String userId = userService.getUserIdByUsername(username);

        assertNull(userId, "User ID should be null when user not found");
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

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession(true)).thenReturn(session);

        User authenticatedUser = userService.authenticateUser(userLoginDTO, request);

        assertNotNull(authenticatedUser, "Authenticated user should not be null");
        assertEquals("testuser", authenticatedUser.getUsername());
    }
}
