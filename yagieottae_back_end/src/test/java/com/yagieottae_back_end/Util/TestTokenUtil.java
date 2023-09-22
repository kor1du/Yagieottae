package com.yagieottae_back_end.Util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yagieottae_back_end.Dto.UserDto;
import com.yagieottae_back_end.Dto.JwtTokenDto;
import com.yagieottae_back_end.Jwt.JwtTokenService;
import com.yagieottae_back_end.Repository.UserRepository;
import com.yagieottae_back_end.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class TestTokenUtil
{
    @Autowired
    private JwtTokenService jwtTokenService;

    public JwtTokenDto getJwtToken(Authentication authentication) throws JsonProcessingException
    {

        JwtTokenDto tokenDto = jwtTokenService.generateToken(authentication);

        return tokenDto;
    }

}
