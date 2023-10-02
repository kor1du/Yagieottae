package com.yagieottae_back_end.Controller.Review;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yagieottae_back_end.Component.TestBase;
import com.yagieottae_back_end.Configuration.TestMockConfig;
import com.yagieottae_back_end.Dto.ResponseDto;
import com.yagieottae_back_end.Dto.ReviewDto;
import com.yagieottae_back_end.Entity.Pill;
import com.yagieottae_back_end.Entity.Review;
import com.yagieottae_back_end.Entity.User;
import com.yagieottae_back_end.Exception.CustomBadRequestException;
import com.yagieottae_back_end.Exception.GlobalControllerExceptionHandler;
import com.yagieottae_back_end.Repository.PillRepository;
import com.yagieottae_back_end.Repository.ReviewRepository;
import com.yagieottae_back_end.Service.PillService;
import com.yagieottae_back_end.Util.TestTokenUtil;
import com.yagieottae_back_end.Util.TestUserUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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

import java.lang.reflect.Field;
import java.util.Date;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TestRead extends TestBase
{
    private final String url = "/review/read";

    //mockTest
    private ResultActions excuteMockTest() throws Exception
    {
        return mockMvc.perform(get(url).params(params));
    }

    //서버 응답 검증
    private void validateServerResponse(ResultActions resultActions) throws Exception
    {
        resultActions
                .andExpect(jsonPath("$.httpStatus").value(expectedResponseDto.getHttpStatus()))
                .andExpect(jsonPath("$.message").value(expectedResponseDto.getMessage()));

        String responseString = resultActions
                .andReturn()
                .getResponse()
                .getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseString);

        System.out.println("result \n" + responseJson.toPrettyString());
    }

    @Test
    @DisplayName("[200][리뷰 조회]")
    public void read() throws Exception
    {
        //given
        setExpectedResponseDto(HttpStatus.OK.value(), "리뷰 목록 조회 성공", null);
        params.add("pillId", "1");

        //when
        ResultActions resultActions = excuteMockTest();

        //then
        validateServerResponse(resultActions);
    }
}

