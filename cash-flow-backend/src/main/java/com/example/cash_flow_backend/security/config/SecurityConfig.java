package com.example.cash_flow_backend.security.config;

import com.example.cash_flow_backend.security.config.filter.JwtFilter;
import com.example.cash_flow_backend.security.service.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private MyUserDetailsService detailsService;
    private JwtFilter jwtFilter;

    public SecurityConfig(MyUserDetailsService detailsService, JwtFilter jwtFilter) {
        this.detailsService = detailsService;
        this.jwtFilter = jwtFilter;
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(this.detailsService);
        provider.setPasswordEncoder(this.bCryptPasswordEncoder());
        return provider;
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder(12);
    }
    @Bean
    public SecurityFilterChain authenticationManager(HttpSecurity security) throws Exception {
        return security.csrf(customize -> customize.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(request -> {
                    request.requestMatchers("**/login", "**/registry").permitAll();
                    request.requestMatchers("**/user/**", "**/admin/**").hasAnyRole("USER", "ADMIN");
                    request.requestMatchers("**/admin/**").hasRole("ADMIN");
                    request.anyRequest().authenticated().anyRequest();
                })
                .addFilterBefore(this.jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
