package com.yagieottae_back_end.Controller.User;

import com.fasterxml.jackson.databind.JsonNode;
import com.yagieottae_back_end.Component.TestBase;
import com.yagieottae_back_end.Configuration.TestMockConfig;
import com.yagieottae_back_end.Dto.ResponseDto;
import com.yagieottae_back_end.Dto.UserDto;
import com.yagieottae_back_end.Entity.User;
import com.yagieottae_back_end.Exception.CustomBadRequestException;
import com.yagieottae_back_end.Jwt.JwtTokenService;
import com.yagieottae_back_end.Repository.UserRepository;
import com.yagieottae_back_end.Service.UserService;
import com.yagieottae_back_end.Util.JsonUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

public class TestLogin extends TestBase
{
    private final String url = "/user/login";
    private UserDto.Login loginDto;
    @Autowired
    private UserService userService;

    //mockTest
    private ResultActions excuteMockTest() throws Exception
    {
        return mockMvc.perform(post(url)
                .content(objectMapper.writeValueAsString(this.loginDto))
                .contentType(MediaType.APPLICATION_JSON));
    }

    private void validateServerResponse(ResultActions resultActions) throws Exception
    {
        resultActions
                .andExpect(jsonPath("$.httpStatus").value(expectedResponseDto.getHttpStatus()))
                .andExpect(jsonPath("$.message").value(expectedResponseDto.getMessage()))
                .andReturn();

        String responseString = resultActions
                .andReturn()
                .getResponse()
                .getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseString);

        if (!responseJson
                .get("body")
                .isNull()) //body 검증
        {
            JsonNode responseBody = responseJson.get("body");

            //userInfo 검증
            if (responseBody.get("userInfo") != null)
            {
                String expectedUserInfo = expectedResponseDto
                        .getBody()
                        .get("userInfo")
                        .toString();

                String actualUserInfo = responseBody
                        .get("userInfo")
                        .toString();

                assertThat(actualUserInfo).isEqualTo(expectedUserInfo);
            }
            //jwtToken 미검증(테스트할때마다 토큰 값, 유효기간이 달라짐)
        }

        System.out.println("서버 응답: \n" + responseJson.toPrettyString());
    }

    //Field 유효성 검사용 메서드
    private static Stream<Arguments> validateTestCases()
    {
        //@formatter:off
        return Stream.of(
                Arguments.of("userId", null, "아이디를 입력해주세요!"),
                Arguments.of("userId", "", "아이디를 입력해주세요!"),
                Arguments.of("password", null, "비밀번호를 입력해주세요!"),
                Arguments.of("password", "", "비밀번호를 입력해주세요!"));
        //@formatter:on
    }

    @Test
    @DisplayName("[200][로그인]")
    public void login() throws Exception
    {
        loginDto = UserDto.Login
                .builder()
                .userId("user")
                .password("1234")
                .build();

        expectedResponseDto = userService.login(loginDto);

        //when
        ResultActions resultActions = excuteMockTest();

        //then
        validateServerResponse(resultActions);
    }

    @ParameterizedTest
    @MethodSource("validateTestCases")
    @DisplayName("[400][로그인] 필드 유효성 검증")
    public void validationCheck(String field, String value, String errorMessage) throws Exception
    {
        //given
        loginDto = UserDto.Login
                .builder()
                .userId("user")
                .password("1234")
                .build();

        setExpectedResponseDto(HttpStatus.BAD_REQUEST.value(), errorMessage, null);

        Field fieldToSet = loginDto
                .getClass()
                .getDeclaredField(field);
        fieldToSet.setAccessible(true);
        fieldToSet.set(loginDto, value);

        //when
        ResultActions resultActions = excuteMockTest();

        //then
        validateServerResponse(resultActions);
    }

    @Test
    @DisplayName("[400][로그인] 존재하지 않는 유저 ID로 로그인")
    public void userNotFound() throws Exception
    {
        //given
        loginDto = UserDto.Login
                .builder()
                .userId("notValidateUserId")
                .password("1234")
                .build();

        setExpectedResponseDto(HttpStatus.BAD_REQUEST.value(), "해당 유저가 존재하지 않습니다!", null);

        //when
        ResultActions resultActions = excuteMockTest();

        //then
        validateServerResponse(resultActions);
    }

    @Test
    @DisplayName("[400][로그인] 비밀번호 불일치")
    public void passwordNotMatch() throws Exception
    {
        //given
        setExpectedResponseDto(HttpStatus.BAD_REQUEST.value(), "비밀번호가 일치하지 않습니다. 입력하신 내용을 다시 확인해주세요.", null);

        loginDto = UserDto.Login
                .builder()
                .userId("user")
                .password("ThisIsWrongPassword")
                .build();

        //when
        ResultActions resultActions = excuteMockTest();

        //then
        validateServerResponse(resultActions);
    }
}
