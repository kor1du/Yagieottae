package com.yagieottae_back_end.Controller.Alram;

import com.fasterxml.jackson.databind.JsonNode;
import com.yagieottae_back_end.Component.TestBase;
import com.yagieottae_back_end.Dto.AlramDto;
import com.yagieottae_back_end.Entity.Alram;
import com.yagieottae_back_end.Repository.AlramRepository;
import com.yagieottae_back_end.Util.JsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class TestGetAlrams extends TestBase
{
    private final String url = "/alram/getAlrams";
    @Autowired
    private AlramRepository alramRepository;

    //MockTest
    private ResultActions excuteMockTest() throws Exception
    {
        return mockMvc.perform(get(url)
                .header("Authorization", "Bearer " + accessToken)
                .params(params));
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

        if (!responseJson
                .get("body")
                .isNull()) //body 검증
        {
            JsonNode responseBody = responseJson.get("body");

            if (responseBody.get("alrams") != null)
            {
                Assertions.assertEquals(responseBody
                        .get("alrams")
                        .toString(), expectedResponseDto
                        .getBody()
                        .get("alrams")
                        .toString());
            }
        }

        System.out.println("result \n" + responseJson.toPrettyString());
    }

    @Test
    @DisplayName("[200][알람 목록 조회] 오늘 날짜에 해당하는 요일의 알람 목록들만 조회")
    public void getAlramsOnlyToday() throws Exception
    {
        //given
        params.set("getToday", "true");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.KOREAN);
        List<AlramDto.Response> alramList = alramRepository
                .findTodayAlrams(user.getId(), dayOfWeek)
                .get();

        setExpectedResponseDto(HttpStatus.OK.value(), "알람 조회 성공", JsonUtil.ObjectToJsonObject("alrams", alramList));

        //when
        ResultActions resultActions = excuteMockTest();

        //then
        validateServerResponse(resultActions);
    }

    @Test
    @DisplayName("[200][알람 목록 조회] 모든 요일의 알람 목록 조회")
    public void getAlramsAll() throws Exception
    {
        //given
        params.set("getToday", "false");
        List<AlramDto.Response> alramList = alramRepository
                .findAllAlrams(user.getId())
                .get(); //전체 알람 목록을 DB에서 조회

        setExpectedResponseDto(HttpStatus.OK.value(), "알람 조회 성공", JsonUtil.ObjectToJsonObject("alrams", alramList));

        //when
        ResultActions resultActions = excuteMockTest();

        //then
        validateServerResponse(resultActions);
    }
}
