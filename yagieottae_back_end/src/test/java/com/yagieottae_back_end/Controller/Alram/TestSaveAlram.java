package com.yagieottae_back_end.Controller.Alram;

import com.fasterxml.jackson.databind.JsonNode;
import com.yagieottae_back_end.Component.TestBase;
import com.yagieottae_back_end.Dto.AlramDto;
import com.yagieottae_back_end.Entity.Alram;
import com.yagieottae_back_end.Entity.Pill;
import com.yagieottae_back_end.Repository.AlramRepository;
import com.yagieottae_back_end.Repository.PillRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.lang.reflect.Field;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class TestSaveAlram extends TestBase
{
    @Autowired
    private AlramRepository alramRepository;
    @Autowired
    private PillRepository pillRepository;
    private AlramDto.Request alramRequestDto;
    private final String url = "/alram/save";

    //Field 유효성 검사용 메서드
    private static Stream<Arguments> validationFields()
    {
        return Stream.of(
                Arguments.of("alramTime", null, "알람 시간을 입력해주세요!"),
                Arguments.of("alramTime", "", "알람 시간을 입력해주세요!"),
                Arguments.of("days", null, "요일을 선택해주세요!"),
                Arguments.of("days", "", "요일을 선택해주세요!"),
                Arguments.of("beforeMeal", null, "식전 / 식후 복용을 선택해주세요!"),
                Arguments.of("dosingTime", 61, "복용시간은 60분 내외로만 설정 가능합니다!"),
                Arguments.of("dosingTime", 0, "복용시간은 0보다 큰 숫자여야 합니다!"),
                Arguments.of("dosingTime", -1, "복용시간은 0보다 큰 숫자여야 합니다!"),
                Arguments.of("pillId", null, "약 정보가 존재하지 않습니다!"));
    }

    //MockTest
    private ResultActions excuteMockTest() throws Exception
    {
        return mockMvc.perform(post(url)
                .header("Authorization", "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(alramRequestDto))
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

        System.out.println("result \n" + responseJson.toPrettyString());
    }

    @Test
    @DisplayName("[200][알람 저장]")
    public void saveAlram() throws Exception
    {
        //given
        Pill pill = pillRepository
                .findById(2L)
                .get();
        setExpectedResponseDto(HttpStatus.OK.value(), String.format("%s의 알람이 저장되었습니다!", pill.getItemName()), null);
        alramRequestDto = AlramDto.Request
                .builder()
                .id(0L)
                .alramTime("12:34")
                .days("1,2,3,4,5,6,7")
                .beforeMeal(true)
                .dosingTime(25)
                .pillId(pill.getId())
                .build();

        //when
        ResultActions resultActions = excuteMockTest();

        //then
        validateServerResponse(resultActions);

        Alram requestAlram = modelMapper.map(alramRequestDto, Alram.class); //서버에 요청한 알람 Dto를 Entity로 변환
        requestAlram = requestAlram
                .toBuilder()
                .pill(pill)
                .build();

        Alram lastSavedAlram = alramRepository
                .findLastAlram()
                .get(); //서버에 마지막으로 저장된 알람

        assertThat(lastSavedAlram)
                .usingRecursiveComparison()
                .ignoringFields("id", "user", "regDate", "editDate") //서버에 저장된 알람에서 id,user,regDate,editDate는 제외하고 검사
                .isEqualTo(requestAlram);
    }

    @Test
    @DisplayName("[200][알람 수정]")
    @Transactional
    public void updateAlram() throws Exception
    {
        //given
        Alram existingAlram = alramRepository.findLastAlram().get();
        Pill existingPill = existingAlram.getPill();

        alramRequestDto = AlramDto.Request.builder() //기존 알람 값에서 수정 한 값으로 Dto 생성
                .id(existingAlram.getId())
                .alramTime("00:00")
                .days("1,3,5")
                .beforeMeal(!existingAlram.getBeforeMeal())
                .dosingTime(15)
                .pillId(existingPill.getId())
                .build();

        setExpectedResponseDto(HttpStatus.OK.value(), String.format("%s의 알람이 저장되었습니다!", existingPill.getItemName()), null);

        //when
        ResultActions resultActions = excuteMockTest();

        //then
        validateServerResponse(resultActions);

        Alram requestAlram = modelMapper.map(alramRequestDto, Alram.class); //서버에 요청한 알람 Dto를 Entity로 변환

        Alram updatedAlram = alramRepository.findLastAlram().get(); //수정된 알람 정보

        assertThat(updatedAlram)
                .usingRecursiveComparison()
                .ignoringFields("user", "regDate", "editDate", "pill")
                .isEqualTo(requestAlram);
    }

    @ParameterizedTest
    @MethodSource("validationFields")
    @DisplayName("[400][알람 수정] 필드 유효성 검증")
    public void validateField(String field, Object value, String errorMessage) throws Exception
    {
        //given
        Pill pill = pillRepository.findById(2L).get();
        setExpectedResponseDto(HttpStatus.BAD_REQUEST.value(), errorMessage, null);

        alramRequestDto = AlramDto.Request
                .builder()
                .id(0L)
                .alramTime("12:34")
                .days("1,2,3,4,5,6,7")
                .beforeMeal(true)
                .dosingTime(25)
                .pillId(pill.getId())
                .build();

        Field fieldToSet = alramRequestDto
                .getClass()
                .getDeclaredField(field);
        fieldToSet.setAccessible(true);
        fieldToSet.set(alramRequestDto, value);

        //when
        ResultActions resultActions = excuteMockTest();

        //then
        validateServerResponse(resultActions);
    }

    @Test
    @DisplayName("[400][알람 저장] 하나의 약을 두개 이상의 알람에 저장")
    public void SaveOneDrugInMoreThanOneAlarm() throws Exception
    {
        //given
        setExpectedResponseDto(HttpStatus.BAD_REQUEST.value(), "기존에 설정해둔 알람이 존재합니다. 알람은 약 하나당 한개씩만 설정 가능합니다.", null);

        Pill pill = pillRepository.findById(1L).get();

        alramRequestDto = AlramDto.Request
                .builder()
                .id(0L)
                .alramTime("12:34")
                .days("1,2,3,4,5,6,7")
                .beforeMeal(true)
                .dosingTime(25)
                .pillId(pill.getId())
                .build();

        //when
        ResultActions resultActions = excuteMockTest();

        //then
        validateServerResponse(resultActions);
    }
}
