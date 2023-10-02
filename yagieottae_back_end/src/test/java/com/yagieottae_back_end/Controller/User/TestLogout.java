package com.yagieottae_back_end.Controller.User;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yagieottae_back_end.Component.TestBase;
import com.yagieottae_back_end.Configuration.TestMockConfig;
import com.yagieottae_back_end.Dto.ResponseDto;
import com.yagieottae_back_end.Dto.UserDto;
import com.yagieottae_back_end.Exception.CustomBadRequestException;
import com.yagieottae_back_end.Exception.GlobalControllerExceptionHandler;
import com.yagieottae_back_end.Repository.UserRepository;
import com.yagieottae_back_end.Service.UserService;
import com.yagieottae_back_end.Util.TestTokenUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TestLogout extends TestBase
{
    private final String url = "/user/logout";

    //mockTest
    private ResultActions excuteMockTest() throws Exception
    {
        return mockMvc.perform(post(url).params(params));
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

        System.out.println("서버 응답: \n" + responseJson.toPrettyString());
    }

    //Field 유효성 검사용 메서드
    private static Stream<Arguments> validateTestCases()
    {
        //@formatter:off
        return Stream.of(
                Arguments.of("accessToken", null, "로그아웃할 회원 정보가 존재하지 않습니다!"),
                Arguments.of("accessToken", "", "로그아웃할 회원 정보가 존재하지 않습니다!")
        );
        //@formatter:on
    }

    @Test
    @DisplayName("[200][로그아웃]")
    public void logout() throws Exception
    {
        //given
        params.add("accessToken", accessToken);

        setExpectedResponseDto(HttpStatus.OK.value(), "로그아웃 되었습니다.", null);

        //when
        ResultActions resultActions = excuteMockTest();

        //then
        validateServerResponse(resultActions);
    }


    @Test
    @DisplayName("[400][로그아웃] 로그아웃된 토큰으로 사용자 인증 시도")
    public void reuseLogouttedToken() throws Exception
    {
        //given
        logout(); //로그아웃

        setExpectedResponseDto(HttpStatus.UNAUTHORIZED.value(), "로그아웃된 토큰입니다!", null);

        //when
        ResultActions resultActions = mockMvc.perform(get("/alram/findAlrams")
                .header("Authorization", "Bearer " + accessToken)
                .params(params));

        //then
        resultActions
                .andExpect(jsonPath("$.httpStatus").value(expectedResponseDto.getHttpStatus()))
                .andExpect(jsonPath("$.message").value(expectedResponseDto.getMessage()))
                .andReturn();

        String responseString = resultActions
                .andReturn()
                .getResponse()
                .getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseString);

        System.out.println("서버 응답: \n" + responseJson.toPrettyString());
    }
}
