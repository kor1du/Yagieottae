package com.yagieottae_back_end.Controller.User;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yagieottae_back_end.Component.TestBase;
import com.yagieottae_back_end.Configuration.TestMockConfig;
import com.yagieottae_back_end.Dto.ResponseDto;
import com.yagieottae_back_end.Exception.CustomBadRequestException;
import com.yagieottae_back_end.Exception.GlobalControllerExceptionHandler;
import com.yagieottae_back_end.Dto.JwtTokenDto;
import com.yagieottae_back_end.Repository.UserRepository;
import com.yagieottae_back_end.Service.UserService;
import com.yagieottae_back_end.Util.TestTokenUtil;
import io.jsonwebtoken.JwtException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

public class TestReissue extends TestBase
{
    private final String url = "/user/reissue";
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //mockTest
    private ResultActions excuteMockTest(ResponseDto expectedResponseDto) throws Exception
    {
        return mockMvc.perform(post(url).param("refreshToken", refreshToken));
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

        //refreshToken 초기화
        //@formatter:off
        JsonNode newTokenJson=responseJson.get("body").get("jwtTokenDto");
        JwtTokenDto newTokenDto = JwtTokenDto.builder()
                .accessToken(newTokenJson.get("accessToken").textValue())
                .refreshToken(newTokenJson.get("refreshToken").textValue())
                .build();
        //@formatter:on
        accessToken = newTokenDto.getAccessToken();
        refreshToken = newTokenDto.getRefreshToken();

        System.out.println("서버 응답: \n" + responseJson.toPrettyString());
    }

    @Test
    @DisplayName("[200][토큰 재발급]")
    public void reissue() throws Exception
    {
        //given
        setExpectedResponseDto(HttpStatus.OK.value(), "토큰이 재발급 되었습니다.", null);

        //when
        ResultActions resultActions = excuteMockTest(expectedResponseDto);

        //then
        validateServerResponse(resultActions);
        String redisRefreshToken = redisTemplate
                .opsForValue()
                .get("RT:" + user.getUserId()); //redis에 저장되어있는 refreshToken 정보
        assertThat(refreshToken).isEqualTo(redisRefreshToken);
    }
}
