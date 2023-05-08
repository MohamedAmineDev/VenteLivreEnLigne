package com.shop.VenteLivreEnLigne.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final PasswordEncoder passwordEncoder;

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        return new InMemoryUserDetailsManager(
//                User.withUsername("user1").password("{noop}123").roles("USER").build(),
//                User.withUsername("user2").password("{noop}123").roles("USER").build(),
//                User.withUsername("user3").password("{noop}123").roles("USER").build(),
//                User.withUsername("admin").password("{noop}123").roles("USER", "ADMIN").build()
                User.withUsername("user1").password(passwordEncoder.encode("123")).roles("USER").build(),
                User.withUsername("user2").password(passwordEncoder.encode("123")).roles("USER").build(),
                User.withUsername("user3").password(passwordEncoder.encode("123")).roles("USER").build(),
                User.withUsername("admin").password(passwordEncoder.encode("123")).roles("USER", "ADMIN").build()
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.formLogin().loginPage("/login").permitAll();
        httpSecurity.rememberMe();
        httpSecurity.authorizeHttpRequests().requestMatchers("/webjars/**").permitAll();
        httpSecurity.authorizeHttpRequests().requestMatchers("/user/**").hasRole("USER");
        httpSecurity.authorizeHttpRequests().requestMatchers("/admin/**").hasRole("ADMIN");
        httpSecurity.authorizeHttpRequests().anyRequest().authenticated();
        httpSecurity.exceptionHandling().accessDeniedPage("/not_authorized");
        return httpSecurity.build();
    }
}
