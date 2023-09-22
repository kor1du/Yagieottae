package com.yagieottae_back_end.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.yagieottae_back_end.Configuration.TestMockConfig;
import com.yagieottae_back_end.Dto.JwtTokenDto;
import com.yagieottae_back_end.Dto.ResponseDto;
import com.yagieottae_back_end.Dto.UserDto;
import com.yagieottae_back_end.Entity.User;
import com.yagieottae_back_end.Exception.CustomBadRequestException;
import com.yagieottae_back_end.Jwt.JwtCustomUserDetails;
import com.yagieottae_back_end.Jwt.JwtCustomUserDetailsService;
import com.yagieottae_back_end.Jwt.JwtTokenService;
import com.yagieottae_back_end.Service.UserService;
import com.yagieottae_back_end.Util.TestTokenUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class TestBase
{
    protected ResponseDto expectedResponseDto;
    protected String accessToken;
    protected String refreshToken;
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ModelMapper modelMapper;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected JwtTokenService jwtTokenService;

    @BeforeEach
    public void beforeEach(TestInfo testInfo) throws Exception
    {
        log.info("{} 테스트 시작", testInfo.getDisplayName());
        initializeTestBaseData();
    }

    @AfterEach
    public void afterEach(TestInfo testInfo)
    {
        log.info("{} 테스트 끝", testInfo.getDisplayName());
    }

    protected void setExpectedResponseDto(int httpStatusCode, String message, ObjectNode body)
    {
        if (body == null)
        {
            expectedResponseDto = ResponseDto.builder()
                                             .httpStatus(httpStatusCode)
                                             .message(message)
                                             .build();
        } else
        {
            expectedResponseDto = ResponseDto.builder()
                                             .httpStatus(httpStatusCode)
                                             .message(message)
                                             .body(body)
                                             .build();
        }
    }

    //인증에 필요한 데이터들 초기화
    protected void setAuthorizationDatas(String userId, String password, String authority)
    {
        Authentication authentication = getUserAuthentication(userId, password, authority);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        JwtTokenDto jwtTokenDto = jwtTokenService.generateToken(authentication);
        accessToken = jwtTokenDto.getAccessToken();
        refreshToken = jwtTokenDto.getRefreshToken();
    }

    private Authentication getUserAuthentication(String userId, String password, String authority)
    {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(simpleGrantedAuthority);

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(userId, password, authorities);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        return authenticationToken;
    }

    protected void initializeTestBaseData()
    {
        try
        {
            setAuthorizationDatas("user", "1234", "USER");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
