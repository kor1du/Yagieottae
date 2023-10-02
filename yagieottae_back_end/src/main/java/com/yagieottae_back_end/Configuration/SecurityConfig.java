package com.yagieottae_back_end.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yagieottae_back_end.Exception.GlobalFilterExceptionHandler;
import com.yagieottae_back_end.Jwt.JwtAuthenticationFilter;
import com.yagieottae_back_end.Jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig
{
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    private final JwtTokenService jwtTokenService;
    private final ObjectMapper objectMapper;

    @Bean
    public WebSecurityCustomizer configure()
    {
        return web -> web
                .ignoring()
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                .requestMatchers(
                        //UserController
                        "/user/login", "/user/signup", "/user/reissue", "/user/logout",
                        //PillController
                        "/pill/getPill",
                        //ReviewController
                        "/review/read",
                        //FileController
                        "/upload/",
                        //Swagger API
                        "/swagger-ui/**", "/v3/api-docs/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        http
                .csrf()
                .disable()
                .cors()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()

                //User Controller Request Matchers
                .requestMatchers("/user/info", "/user/updateInfo")
                .hasAuthority("USER")

                //Alram Controller Request Matchers
                .requestMatchers("/alram/save", "/alram/findAlrams")
                .hasAuthority("USER")

                //Review Controller Request Matchers
                .requestMatchers("/review/save", "review/delete")
                .hasAuthority("USER")

                .anyRequest()
                .authenticated()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new GlobalFilterExceptionHandler(objectMapper), JwtAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource()
    {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
