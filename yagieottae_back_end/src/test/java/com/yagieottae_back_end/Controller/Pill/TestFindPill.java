package com.yagieottae_back_end.Controller.Alram;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yagieottae_back_end.Component.TestBase;
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
import com.yagieottae_back_end.Service.PillService;
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
public class TestFindPill extends TestBase
{
    private String itemName;
    @Autowired
    PillService pillService;

    @BeforeEach
    public void beforeEach(TestInfo testInfo) throws Exception
    {
        log.info("{} 테스트 시작", testInfo.getDisplayName());
        initializeTestBaseData();
        initializeTestData();
    }

    //mockTest
    private void doMockTest(ResponseDto expectedResponseDto, ResultMatcher status) throws Exception
    {
        MvcResult result = mockMvc.perform(get("/pill/findPill")
                                          .param("itemName", itemName))
                                  .andExpect(status)
                                  .andExpect(jsonPath("$.httpStatus").value(expectedResponseDto.getHttpStatus()))
                                  .andExpect(jsonPath("$.message").value(expectedResponseDto.getMessage()))
                                  .andReturn();

        String responseString = result.getResponse().getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseString);

        if (!responseJson.get("body").isNull()) //body 검증
        {
            JsonNode responseBody = responseJson.get("body");

            if (responseBody.get("pillList") != null)
            {
                Assertions.assertEquals(responseBody.get("pillList").toString(), expectedResponseDto.getBody().get("pillList").toString());
            }
        }

        System.out.println("서버 응답: \n" + responseJson.toPrettyString());
    }

    //데이터 초기화
    private void initializeTestData() throws Exception
    {
        itemName = "베아제";
    }


    @Test
    @DisplayName("[200][약 조회]")
    @Transactional
    public void test_FindPill() throws Exception
    {
        expectedResponseDto = pillService.findPill(itemName);

        doMockTest(expectedResponseDto, status().isOk());
    }

    @Test
    @DisplayName("[400][약 조회] 검색된 약 없음")
    @Transactional
    public void test_FindPill_NotFound() throws Exception
    {
        itemName = "없는 약 이름";

        setExpectedResponseDto(HttpStatus.BAD_REQUEST.value(), "찾으시는 약이 존재하지 않습니다!", null);

        doMockTest(expectedResponseDto, status().isBadRequest());
    }

    @AfterEach
    public void afterEach(TestInfo testInfo)
    {
        log.info("{} 테스트 끝", testInfo.getDisplayName());
    }
}
