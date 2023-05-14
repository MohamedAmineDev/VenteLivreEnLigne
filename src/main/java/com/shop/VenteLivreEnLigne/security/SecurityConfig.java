package com.shop.VenteLivreEnLigne.security;

import com.shop.VenteLivreEnLigne.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;

    //@Bean
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
        httpSecurity.formLogin().loginPage("/login").defaultSuccessUrl("/", true).failureForwardUrl("/login-error").permitAll();
        httpSecurity.rememberMe()
                .key("uniqueAndSecret")
                .userDetailsService(userDetailsService);
        httpSecurity.authorizeHttpRequests().requestMatchers("/webjars/**", "/js/**").permitAll();
        httpSecurity.authorizeHttpRequests().requestMatchers("/register_user", "/login-error").permitAll();
        httpSecurity.authorizeHttpRequests().requestMatchers("/save_user").permitAll();
        httpSecurity.authorizeHttpRequests().requestMatchers("/user/**").hasRole("USER");
        httpSecurity.authorizeHttpRequests().requestMatchers("/client/**").hasRole("CLIENT");
        httpSecurity.authorizeHttpRequests().requestMatchers("/admin/**").hasRole("ADMIN");
        httpSecurity.authorizeHttpRequests().anyRequest().authenticated();
        httpSecurity.exceptionHandling().accessDeniedPage("/not_authorized");
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
        httpSecurity.userDetailsService(userDetailsService);
        return httpSecurity.build();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
