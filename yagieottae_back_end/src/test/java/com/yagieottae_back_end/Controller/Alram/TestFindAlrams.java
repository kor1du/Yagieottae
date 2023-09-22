package com.yagieottae_back_end.Controller.Alram;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yagieottae_back_end.Component.TestBase;
import com.yagieottae_back_end.Configuration.TestMockConfig;
import com.yagieottae_back_end.Dto.ResponseDto;
import com.yagieottae_back_end.Dto.UserDto;
import com.yagieottae_back_end.Entity.User;
import com.yagieottae_back_end.Exception.CustomBadRequestException;
import com.yagieottae_back_end.Exception.GlobalControllerExceptionHandler;
import com.yagieottae_back_end.Repository.AlramRepository;
import com.yagieottae_back_end.Repository.UserRepository;
import com.yagieottae_back_end.Service.AlramService;
import com.yagieottae_back_end.Util.TestTokenUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import({TestMockConfig.class})
@Slf4j
public class TestFindAlrams extends TestBase
{
    private MultiValueMap<String, String> params;
    @Autowired
    AlramService alramService;

    @BeforeEach
    public void beforeTest()
    {
        initializeTestData();
    }

    //mockTest
    private void doMockTest(ResponseDto expectedResponseDto, ResultMatcher status) throws Exception
    {
        MvcResult result = mockMvc.perform(get("/alram/findAlrams")
                                          .header("Authorization", "Bearer " + accessToken)
                                          .params(params))
                                  .andExpect(status)
                                  .andExpect(jsonPath("$.httpStatus").value(expectedResponseDto.getHttpStatus()))
                                  .andExpect(jsonPath("$.message").value(expectedResponseDto.getMessage()))
                                  .andReturn();

        String responseString = result.getResponse().getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseString);

        if (!responseJson.get("body").isNull()) //body 검증
        {
            JsonNode responseBody = responseJson.get("body");

            if (responseBody.get("alrams") != null)
            {
                Assertions.assertEquals(responseBody.get("alrams").toString(), expectedResponseDto.getBody().get("alrams").toString());
            }
        }

        System.out.println("서버 응답: \n" + responseJson.toPrettyString());
    }

    //데이터 초기화
    private void initializeTestData()
    {
        params = new LinkedMultiValueMap<>();
    }

    @Test
    @DisplayName("[200][알람 목록 조회] 오늘 날짜에 해당하는 요일의 알람 목록들만 조회")
    @Transactional
    public void Test_FindTodayAlrams() throws Exception
    {
        params.set("getToday", "true");
        expectedResponseDto = alramService.findAlrams(true);
        doMockTest(expectedResponseDto, status().isOk());
    }

    @Test
    @DisplayName("[200][알람 목록 조회] 모든 요일의 알람 목록 조회")
    @Transactional
    public void Test_FindAllAlrams() throws Exception
    {
        params.set("getToday", "false");
        expectedResponseDto = alramService.findAlrams(false);
        doMockTest(expectedResponseDto, status().isOk());
    }
}
