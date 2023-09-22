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
import org.springframework.test.web.servlet.ResultMatcher;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import({TestMockConfig.class})
@Slf4j
public class TestRead extends TestBase
{
    //mockTest
    private void doMockTest(ResponseDto expectedResponseDto, ResultMatcher status) throws Exception
    {
        MvcResult result = mockMvc.perform(get("/review/read")
                                          .param("pillId", "3488"))
                                  .andExpect(status)
                                  .andExpect(jsonPath("$.httpStatus").value(expectedResponseDto.getHttpStatus()))
                                  .andExpect(jsonPath("$.message").value(expectedResponseDto.getMessage()))
                                  .andReturn();

        String responseString = result.getResponse().getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseString);

        System.out.println("서버 응답: \n" + responseJson.toPrettyString());
    }

    @Test
    @DisplayName("[200][리뷰 조회]")
    @Transactional
    public void test_Read() throws Exception
    {
        setExpectedResponseDto(HttpStatus.OK.value(), "리뷰 목록 조회 성공", null);

        doMockTest(expectedResponseDto, status().isOk());
    }
}

