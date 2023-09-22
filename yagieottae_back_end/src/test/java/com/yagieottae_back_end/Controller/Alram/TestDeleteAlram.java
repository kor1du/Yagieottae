package com.yagieottae_back_end.Controller.Alram;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yagieottae_back_end.Configuration.TestMockConfig;
import com.yagieottae_back_end.Dto.AlramDto;
import com.yagieottae_back_end.Dto.ResponseDto;
import com.yagieottae_back_end.Dto.UserDto;
import com.yagieottae_back_end.Entity.Alram;
import com.yagieottae_back_end.Entity.Pill;
import com.yagieottae_back_end.Entity.User;
import com.yagieottae_back_end.Exception.CustomBadRequestException;
import com.yagieottae_back_end.Exception.GlobalControllerExceptionHandler;
import com.yagieottae_back_end.Repository.AlramRepository;
import com.yagieottae_back_end.Repository.PillRepository;
import com.yagieottae_back_end.Repository.UserRepository;
import com.yagieottae_back_end.Service.AlramService;
import com.yagieottae_back_end.Service.UserService;
import com.yagieottae_back_end.Util.TestTokenUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.lang.reflect.Field;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import({TestMockConfig.class})
@Slf4j
public class TestDeleteAlram
{
    private CustomBadRequestException customBadRequestException;
    private ResponseDto expectedResponseDto;
    private String alramId;
    private String accessToken;
    private User user;
    @Autowired
    AlramService alramService;
    @Autowired
    AlramRepository alramRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private GlobalControllerExceptionHandler globalControllerExceptionHandler;

    //mockTest
    private void doMockTest(ResponseDto expectedResponseDto, ResultMatcher status) throws Exception
    {
        MvcResult result = mockMvc.perform(delete("/alram/deleteAlram")
                                          .header("Authorization", "Bearer " + accessToken)
                                          .param("alramId", alramId))
                                  .andExpect(status)
                                  .andExpect(jsonPath("$.httpStatus").value(HttpStatus.OK.value()))
                                  .andExpect(jsonPath("$.message").value(expectedResponseDto.getMessage()))
                                  .andReturn();

        String responseString = result.getResponse().getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseString);

        System.out.println("서버 응답: \n" + responseJson.toPrettyString());
    }

    //데이터 초기화
    private void initializeTestData() throws Exception
    {
        alramId = alramRepository.findAll().get(0).getId().toString();
    }


    @Test
    @DisplayName("[200][알람 삭제]")
    @Transactional
    public void test_DeleteAlram() throws Exception
    {
        expectedResponseDto = ResponseDto.builder()
                                         .httpStatus(HttpStatus.OK.value())
                                         .message("알람이 삭제되었습니다.")
                                         .build();

        doMockTest(expectedResponseDto, status().isOk());
    }

    @Test
    @DisplayName("[400][알람 삭제] 삭제 대상 없음")
    @Transactional
    public void test_DeleteAlram_AlramNotFound() throws Exception
    {
        alramId = "-1";
        customBadRequestException = new CustomBadRequestException("삭제 대상 알람이 존재하지 않습니다.");
        expectedResponseDto = globalControllerExceptionHandler.handleBadRequestException(customBadRequestException).getBody();

        doMockTest(expectedResponseDto, status().isBadRequest());
    }
}
