package com.the_build_guild.trivia_game.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/users/create").permitAll()
            .requestMatchers("/api/users/login").permitAll()
            .requestMatchers("/api/trivia/play").permitAll()
            .requestMatchers("/api/trivia/status").permitAll()
            .requestMatchers("/api/trivia/saveGame").permitAll()
            .requestMatchers("/api/leaderboard/allTime").permitAll()
            .requestMatchers("/api/users/profile").permitAll()
            .requestMatchers("/api/users/leaderboard/personal").permitAll()
            .requestMatchers("/logout").permitAll()
                .anyRequest().permitAll()
            )
            .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()) // Return 200 OK instead of redirecting
            .permitAll()
        );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000","http://localhost:3000/signin" )); // Adjust this to match your frontend URL
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}