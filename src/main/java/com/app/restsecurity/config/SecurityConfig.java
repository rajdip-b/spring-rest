package com.app.restsecurity.config;

import com.app.restsecurity.security.JWTTokenAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(jwtTokenAuthenticationFilter(), BasicAuthenticationFilter.class);
        http.authorizeRequests()
                .antMatchers("/api/admin/**").authenticated()
                .antMatchers("/api/private/**").authenticated()
                .antMatchers("/api/public/**").permitAll();
        http.authorizeRequests().anyRequest().permitAll();
        http.cors().and().csrf().disable();
        return http.build();
    }

    @Bean
    public JWTTokenAuthenticationFilter jwtTokenAuthenticationFilter() {
        return new JWTTokenAuthenticationFilter();
    }

}
