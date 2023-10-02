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
import com.yagieottae_back_end.Repository.UserRepository;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@SpringBootTest
@Import({TestMockConfig.class})
@Slf4j
public class TestBase
{
    protected final MultiValueMap<String, String> params = new LinkedMultiValueMap<>(); //Parameter List
    protected ResponseDto expectedResponseDto; //예상되는 서버 응답
    protected User user; //유저 객체
    protected String accessToken; //jwtAccessToken
    protected String refreshToken; //jwtRefreshToken
    @Autowired
    protected MockMvc mockMvc; //Mock 객체
    @Autowired
    protected ModelMapper modelMapper; //객체 Mapper
    @Autowired
    protected ObjectMapper objectMapper; //Object ↔ JSON Mapper
    @Autowired
    protected JwtTokenService jwtTokenService; //jwtToken 생성,인증 관련 Service
    @Autowired
    private UserRepository userRepository; //userRepository

    //테스트전 실행
    @BeforeEach
    public void beforeTest(TestInfo testInfo) throws Exception
    {
        log.info("{} 테스트 시작", testInfo.getDisplayName());
        initializeTestBaseData();
    }

    //테스트 후 실행
    @AfterEach
    public void afterTest(TestInfo testInfo)
    {
        log.info("{} 테스트 끝", testInfo.getDisplayName());
    }

    //예상 응답값 세팅
    protected void setExpectedResponseDto(int httpStatusCode, String message, ObjectNode body)
    {
        if (body == null) //body 없음(응답에 응답 코드와 메시지만 존재)
        {
            expectedResponseDto = ResponseDto.builder()
                                             .httpStatus(httpStatusCode)
                                             .message(message)
                                             .build();
        } else //body 존재(응답에 응답 코드, 메시지, body가 존재)
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

    //유저 인증 객체 반환
    private Authentication getUserAuthentication(String userId, String password, String authority)
    {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(simpleGrantedAuthority);

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(userId, password, authorities);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        return authenticationToken;
    }

    //테스트에 필요한 데이터들 초기화
    protected void initializeTestBaseData()
    {
        try
        {
            user = userRepository.findById(1L).orElseThrow(()->new Exception("테스트에 사용될 유저 객체가 존재하지 않습니다!")); //유저 객체 초기화
            setAuthorizationDatas(user.getUserId(), user.getPassword(), user.getRole().toString());
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
