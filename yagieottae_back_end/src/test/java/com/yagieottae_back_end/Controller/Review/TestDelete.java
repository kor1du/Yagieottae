package com.yagieottae_back_end.Controller.Review;

import com.fasterxml.jackson.databind.JsonNode;
import com.yagieottae_back_end.Component.TestBase;
import com.yagieottae_back_end.Configuration.TestMockConfig;
import com.yagieottae_back_end.Dto.ResponseDto;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestDelete extends TestBase
{
    private final String url = "/review/delete";

    private ResultActions excuteMockTest() throws Exception
    {
        return mockMvc.perform(delete(url)
                .params(params)
                .header("Authorization", "Bearer " + accessToken));
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
    @DisplayName("[200][리뷰 삭제]")
    public void deleteReview() throws Exception
    {
        //given
        params.add("reviewId", "1");

        setExpectedResponseDto(HttpStatus.OK.value(), "리뷰가 삭제되었습니다.", null);

        //when
        ResultActions resultActions = excuteMockTest();

        //then
        validateServerResponse(resultActions);
    }

    @Test
    @DisplayName("[400][리뷰 삭제] 존재하지 않는 리뷰 삭제")
    public void notFound() throws Exception
    {
        //given
        params.add("reviewId", "-1");

        setExpectedResponseDto(HttpStatus.BAD_REQUEST.value(), "해당 리뷰가 존재하지 않습니다!", null);

        //when
        ResultActions resultActions = excuteMockTest();

        //then
        validateServerResponse(resultActions);
    }

    @Test
    @DisplayName("[400][리뷰 삭제] 다른 유저가 삭제 요청")
    public void anotherUserRequestedDeletion() throws Exception
    {
        //given
        params.add("reviewId", "1");

        setAuthorizationDatas("userWithoutPermission", "1234", "USER");

        setExpectedResponseDto(HttpStatus.BAD_REQUEST.value(), "삭제 권한이 존재하지 않습니다!", null);

        //when
        ResultActions resultActions = excuteMockTest();

        //then
        validateServerResponse(resultActions);
    }
}
