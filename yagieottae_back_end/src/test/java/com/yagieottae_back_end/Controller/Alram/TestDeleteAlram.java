package com.yagieottae_back_end.Controller.Alram;

import com.fasterxml.jackson.databind.JsonNode;
import com.yagieottae_back_end.Component.TestBase;
import com.yagieottae_back_end.Repository.AlramRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TestDeleteAlram extends TestBase
{
    private final String url = "/alram/deleteAlram";
    @Autowired
    private AlramRepository alramRepository;

    //MockTest
    private ResultActions excuteMockTest() throws Exception
    {
        return mockMvc.perform(delete(url)
                .header("Authorization", "Bearer " + accessToken)
                .params(params));
    }

    //서버 응답 검증
    private void validateServerResponse(ResultActions resultActions) throws Exception{
        resultActions
                .andExpect(jsonPath("$.httpStatus").value(expectedResponseDto.getHttpStatus()))
                .andExpect(jsonPath("$.message").value(expectedResponseDto.getMessage()));

        String responseString = resultActions.andReturn().getResponse().getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseString);

        System.out.println("result \n" + responseJson.toPrettyString());
    }

    @Test
    @DisplayName("[200][알람 삭제]")
    public void deleteAlram() throws Exception
    {
        //given
        String alramId="1";
        params.add("alramId",alramId);
        setExpectedResponseDto(HttpStatus.OK.value(), "알람이 삭제되었습니다.", null);

        //when
        ResultActions resultActions = excuteMockTest();

        //then
        //서버 응답 검증
        validateServerResponse(resultActions);
        //삭제된 알람 서버에 존재하는지 체크
        Assert.assertTrue(alramRepository.findById(Long.parseLong(alramId)).isEmpty());
    }

    @Test
    @DisplayName("[400][알람 삭제] 삭제 대상 없음")
    public void alramNotFound() throws Exception
    {
        //given
        String alramId = "-1";
        setExpectedResponseDto(HttpStatus.BAD_REQUEST.value(), "삭제 대상 알람이 존재하지 않습니다.", null);

        //when
        ResultActions resultActions = excuteMockTest();
        
        //then
        //서버 응답 검증
        validateServerResponse(resultActions);
    }
}
