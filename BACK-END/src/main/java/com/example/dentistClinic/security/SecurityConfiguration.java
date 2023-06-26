package com.example.dentistClinic.security;
import com.example.dentistClinic.jwt.JwtRequestFilter;
import com.example.dentistClinic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService usuarioService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private Environment environment;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }


        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .cors()
                    .and()
                    .authorizeRequests()
                    .antMatchers("/appointments/**").hasAnyAuthority("PATIENT", "ADMIN", "DENTIST")
                    .antMatchers(HttpMethod.POST, "/appointments/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/appointments/random").hasAuthority("PATIENT")
                    .antMatchers(HttpMethod.PUT, "/appointments/**").access("hasAuthority('ADMIN')")
                    .antMatchers(HttpMethod.DELETE, "/appointments/**").access("hasAuthority('ADMIN')")
                    .antMatchers(HttpMethod.POST, "/users/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/confirmToken/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/appointments/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/users/**").permitAll()
                   // .antMatchers(HttpMethod.GET, "/users/{id}").hasAnyAuthority("PATIENT", "ADMIN", "DENTIST")
                    .antMatchers(HttpMethod.GET, "/users/getDentists").hasAuthority("ADMIN")
                    .antMatchers(HttpMethod.POST, "/users/login").permitAll()
                    .antMatchers(HttpMethod.POST, "/videos/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/images/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/videos/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/initial/**").permitAll()
                    .antMatchers("/message/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .logout()
                    .and()
                    .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/ws/**"); // Allow WebSocket requests
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedHeader("*");
        config.addExposedHeader("Authorization");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        config.setAllowedOrigins(Arrays.asList("http://127.0.0.1:5173", "http://127.0.0.1:5174"));
        source.registerCorsConfiguration("/**", config);
        return source;
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
        provider.setUserDetailsService(usuarioService);
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        return provider;
    }

}
