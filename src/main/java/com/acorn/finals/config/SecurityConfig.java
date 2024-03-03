package com.acorn.finals.config;

import com.acorn.finals.config.properties.CorsPropertiesConfig;
import com.acorn.finals.filter.JwtFilter;
import com.acorn.finals.security.handler.CustomAuthenticationFailureHandler;
import com.acorn.finals.security.handler.CustomAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CorsPropertiesConfig corsConfig;
    private final CustomAuthenticationSuccessHandler successHandler;
    private final JwtFilter jwtFilter;
//    private final AcornCorsFilter acornCorsFilter;

    private final String[] whiteList = {
            "/chat/channel/*/topic/*",
            "/connection/ping",
            "/connection/channel/*/members"
    };

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//
//        configuration.setAllowedOrigins(List.of(corsConfig.getAllowedOrigins()));
//        configuration.setAllowedMethods(List.of(CorsConfiguration.ALL));
//        configuration.setAllowCredentials(true);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        http.authorizeHttpRequests(auth ->
                auth
                        .requestMatchers(whiteList).permitAll()
                        .anyRequest().authenticated()
        );

        http.oauth2Login(o -> {
            o.successHandler(successHandler);
            o.failureHandler(new CustomAuthenticationFailureHandler());
            o.permitAll();
        });
        http.sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    PasswordEncoder bcryptEncoder() {
        return new BCryptPasswordEncoder();
    }

}
