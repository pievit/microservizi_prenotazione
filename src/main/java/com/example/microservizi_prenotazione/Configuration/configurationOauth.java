package com.example.microservizi_prenotazione.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class configurationOauth {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .anyRequest().authenticated()
                .and()
                .oauth2Login();
        return http.build();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/api/v1/**").and().ignoring().requestMatchers(HttpMethod.GET,"/").and().ignoring().requestMatchers(HttpMethod.POST).and().ignoring().requestMatchers(HttpMethod.DELETE).and().ignoring().requestMatchers(HttpMethod.PUT);
    }




}

