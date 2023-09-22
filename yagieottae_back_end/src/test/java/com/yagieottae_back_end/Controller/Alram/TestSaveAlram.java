package com.yagieottae_back_end.Controller.Alram;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yagieottae_back_end.Component.TestBase;
import com.yagieottae_back_end.Configuration.TestMockConfig;
import com.yagieottae_back_end.Dto.AlramDto;
import com.yagieottae_back_end.Dto.ResponseDto;
import com.yagieottae_back_end.Entity.Alram;
import com.yagieottae_back_end.Entity.Pill;
import com.yagieottae_back_end.Entity.User;
import com.yagieottae_back_end.Exception.CustomBadRequestException;
import com.yagieottae_back_end.Exception.GlobalControllerExceptionHandler;
import com.yagieottae_back_end.Repository.AlramRepository;
import com.yagieottae_back_end.Repository.PillRepository;
import com.yagieottae_back_end.Repository.UserRepository;
import com.yagieottae_back_end.Service.AlramService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import({TestMockConfig.class})
@Slf4j
public class TestSaveAlram extends TestBase
{
    private AlramDto.Request alramRequestDto;
    private Pill pill;
    @Autowired
    AlramRepository alramRepository;
    @Autowired
    PillRepository pillRepository;

    @BeforeEach
    public void beforeTest() throws Exception
    {
        initializeTestData();
    }

    //mockTest
    private void doMockTest(ResponseDto expectedResponseDto, ResultMatcher status) throws Exception
    {
        MvcResult result = mockMvc.perform(post("/alram/save")
                                          .header("Authorization", "Bearer " + accessToken)
                                          .content(objectMapper.writeValueAsString(alramRequestDto))
                                          .contentType(MediaType.APPLICATION_JSON))
                                  .andExpect(status)
                                  .andExpect(jsonPath("$.httpStatus").value(expectedResponseDto.getHttpStatus()))
                                  .andExpect(jsonPath("$.message").value(expectedResponseDto.getMessage()))
                                  .andReturn();

        String responseString = result.getResponse().getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseString);

        System.out.println("서버 응답: \n" + responseJson.toPrettyString());
    }

    //데이터 초기화
    private void initializeTestData() throws Exception
    {
        pill = pillRepository.findAll().get(0);

        alramRequestDto = AlramDto.Request.builder()
                                          .id(0L)
                                          .alramTime("12:00")
                                          .days("1,2,3,4,5,6,7")
                                          .beforeMeal(true)
                                          .dosingTime(50L)
                                          .pillId(1L)
                                          .build();
    }

    //Field 유효성 검사용 메서드
    private static Stream<Arguments> validationTestCases()
    {
        return Stream.of(
                Arguments.of("alramTime", null, "알람 시간을 입력해주세요!"),
                Arguments.of("alramTime", "", "알람 시간을 입력해주세요!"),
                Arguments.of("days", null, "요일을 선택해주세요!"),
                Arguments.of("days", "", "요일을 선택해주세요!"),
                Arguments.of("beforeMeal", null, "식전 / 식후 복용을 선택해주세요!"),
                Arguments.of("dosingTime", null, "복용 시간을 입력해주세요!"),
                Arguments.of("dosingTime", 61L, "복용시간은 60분 내외로만 설정 가능합니다!"),
                Arguments.of("dosingTime", 0L, "복용시간은 0보다 큰 숫자여야 합니다!"),
                Arguments.of("dosingTime", -1L, "복용시간은 0보다 큰 숫자여야 합니다!"),
                Arguments.of("pillId", null, "약 정보가 존재하지 않습니다!")
        );
    }

    @Test
    @DisplayName("[200][알람 저장]")
    @Transactional
    public void test_SaveAlram() throws Exception
    {
        setExpectedResponseDto(HttpStatus.OK.value(), String.format("%s의 알람이 저장되었습니다!", pill.getItemName()), null);

        doMockTest(expectedResponseDto, status().isOk());
    }

    @Test
    @DisplayName("[200][알람 수정]")
    @Transactional
    public void test_SaveAlram_Update() throws Exception
    {
        Alram existedAlram = alramRepository.findAll().get(0); //기존 알람

        alramRequestDto = AlramDto.Request.builder() //기존 알람 값 변경
                                          .id(existedAlram.getId())
                                          .alramTime("12:34")
                                          .days("1,3")
                                          .beforeMeal(!existedAlram.getBeforeMeal())
                                          .dosingTime(15L)
                                          .pillId(existedAlram.getPill().getId())
                                          .build();

        setExpectedResponseDto(HttpStatus.OK.value(), String.format("%s의 알람이 저장되었습니다!", pill.getItemName()), null);

        doMockTest(expectedResponseDto, status().isOk()); //알람 수정

        existedAlram = alramRepository.findAll().get(0); //기존 알람

        AlramDto.Request updatedAlram = modelMapper.map(existedAlram, AlramDto.Request.class); //수정된 알람 정보

        Assertions.assertEquals(alramRequestDto, updatedAlram);
    }

    @ParameterizedTest
    @MethodSource("validationTestCases")
    @DisplayName("[400][알람 수정] 필드 유효성 검증")
    public void test_SaveAlram_ValidationCheck(String field, Object value, String errorMessage) throws Exception
    {
        setExpectedResponseDto(HttpStatus.BAD_REQUEST.value(), errorMessage, null);

        Field fieldToSet = alramRequestDto.getClass().getDeclaredField(field);
        fieldToSet.setAccessible(true);
        fieldToSet.set(alramRequestDto, value);

        doMockTest(expectedResponseDto, status().isBadRequest());
    }

    @Test
    @DisplayName("[400][알람 저장] 하나의 약을 두개 이상의 알람에 저장")
    @Transactional
    public void test_SaveAlram_SaveOneDrugInMoreThanOneAlarm() throws Exception
    {
        setExpectedResponseDto(HttpStatus.BAD_REQUEST.value(), "기존에 설정해둔 알람이 존재합니다. 알람은 약 하나당 한개씩만 설정 가능합니다.", null);

        doMockTest(expectedResponseDto, status().isBadRequest());
    }

    @Test
    @DisplayName("[400][알람 저장] 존재하지 않는 pk값으로 조회")
    @Transactional
    public void test_SaveAlram_NotAvaliblePublicKey() throws Exception
    {
        setExpectedResponseDto(HttpStatus.BAD_REQUEST.value(), "기존 알람 정보가 존재하지 않습니다!", null);

        alramRequestDto.setId(-1L);

        doMockTest(expectedResponseDto, status().isBadRequest());
    }

    @AfterEach
    public void afterEach(TestInfo testInfo)
    {
        log.info("{} 테스트 끝", testInfo.getDisplayName());
    }
}
