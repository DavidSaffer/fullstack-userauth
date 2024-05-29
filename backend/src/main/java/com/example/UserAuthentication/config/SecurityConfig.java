package com.example.UserAuthentication.config;

import com.example.UserAuthentication.utility.JwtRequestFilter;
import com.example.UserAuthentication.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration class using Spring Security to enforce security measures across the application.
 * This configuration specifies security behaviors including disabling CSRF, managing static and API endpoint securities,
 * and disabling form login due to the reliance on JWT for authentication.
 *
 * Security Setup:
 * - CSRF Protection: Disabled because JWT is used, which is inherently secure against CSRF attacks.
 * - Public Endpoints: Configures certain URL patterns such as static resources ('/', '/index.html', '/css/**', '/images/**')
 *   and authentication related paths ('/api/auth/**') to be accessible without authentication.
 * - Form Login: Disabled to utilize token-based authentication provided by JWT.
 * - JWT Filter: (NOT USED) A custom JWT request filter is configured to intercept and validate JWTs in API requests.
 *
 * This setup ensures that the application is secure while still allowing public access to necessary resources.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter(jwtUtil);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/", "/index.html", "/index", "/app.js", "/css/**", "/images/**").permitAll() // Allow all these paths.
                                .requestMatchers("/api/auth/**").permitAll()
                                .anyRequest().permitAll()

                                )
                .formLogin(formLogin -> formLogin.disable());

        return http.build();
    }

    /**
     * Password encoder bean to hash and verify passwords in the application.
     * Uses BCrypt hashing algorithm.
     *
     * @return a BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Default strength is 10
    }
}
