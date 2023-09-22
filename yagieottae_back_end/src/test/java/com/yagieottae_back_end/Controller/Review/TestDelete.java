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
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import({TestMockConfig.class})
@Slf4j
public class TestDelete extends TestBase
{
    private String reviewId;

    //테스트 데이터 생성자로 초기화
    TestDelete()
    {
        reviewId = "1";
    }

    private void doMockTest(ResponseDto expectedResponseDto, ResultMatcher status) throws Exception
    {
        MvcResult result = mockMvc.perform(delete("/review/delete")
                                          .param("reviewId", reviewId)
                                          .header("Authorization", "Bearer " + accessToken))
                                  .andExpect(status)
                                  .andExpect(jsonPath("$.httpStatus").value(expectedResponseDto.getHttpStatus()))
                                  .andExpect(jsonPath("$.message").value(expectedResponseDto.getMessage()))
                                  .andReturn();

        String responseString = result.getResponse().getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseString);

        System.out.println("서버 응답: \n" + responseJson.toPrettyString());
    }

    @Test
    @DisplayName("[200][리뷰 삭제]")
    @Transactional
    public void test_Delete() throws Exception
    {
        setExpectedResponseDto(HttpStatus.OK.value(), "리뷰가 삭제되었습니다.", null);

        doMockTest(expectedResponseDto, status().isOk());
    }

    @Test
    @DisplayName("[400][리뷰 삭제] 존재하지 않는 리뷰 삭제")
    @Transactional
    public void test_Delete_NotFound() throws Exception
    {
        reviewId = "-1";

        setExpectedResponseDto(HttpStatus.BAD_REQUEST.value(), "해당 리뷰가 존재하지 않습니다!", null);

        doMockTest(expectedResponseDto, status().isBadRequest());
    }

    @Test
    @DisplayName("[400][리뷰 삭제] 다른 유저가 삭제 요청")
    @Transactional
    public void test_Delete_AnotherUserRequestedDeletion() throws Exception
    {
        setAuthorizationDatas("userWithoutPermission", "1234", "USER");

        setExpectedResponseDto(HttpStatus.BAD_REQUEST.value(), "삭제 권한이 존재하지 않습니다!", null);

        doMockTest(expectedResponseDto, status().isBadRequest());
    }
}
