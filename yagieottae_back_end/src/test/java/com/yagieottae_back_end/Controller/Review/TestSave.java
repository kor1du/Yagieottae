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
import com.yagieottae_back_end.Util.JsonUtil;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

public class TestSave extends TestBase
{
    private ReviewDto.Request reviewRequestDto;
    @Autowired
    private ReviewRepository reviewRepository;

    //mockTest
    private ResultActions excuteMockTest() throws Exception
    {
        return mockMvc.perform(post("/review/save")
                .header("Authorization", "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(reviewRequestDto))
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

    //Field 유효성 검사용 메서드
    private static Stream<Arguments> validateTestCases()
    {
        //@formatter:off
        return Stream.of(
                Arguments.of("rate", 0, "별점은 1점이상으로만 입력 가능합니다!"),
                Arguments.of("rate", 6, "별점은 5점까지만 입력 가능합니다!"),
                Arguments.of("reviewMessage", null, "리뷰 메시지를 입력해주세요!"),
                Arguments.of("reviewMessage", "", "리뷰 메시지를 입력해주세요!"),
                Arguments.of("reviewMessage", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec quis lacus vitae nibh pulvinar ornare.", "최대 100자까지만 입력이 가능합니다!")
        );
        //@formatter:on
    }

    @Test
    @DisplayName("[200][리뷰 작성]")
    public void save() throws Exception
    {
        //given
        setExpectedResponseDto(HttpStatus.OK.value(), "리뷰가 정상적으로 등록되었습니다.", null);

        reviewRequestDto = ReviewDto.Request
                .builder()
                .id(0L)
                .rate(3)
                .reviewMessage("리뷰 등록 메시지 TEST")
                .pillId(1L)
                .regDate(new Date())
                .editDate(new Date())
                .build();

        //when
        ResultActions resultActions = excuteMockTest();

        //then
        validateServerResponse(resultActions);
    }

    @Test
    @DisplayName("[200][리뷰 작성] 수정")
    @Transactional
    public void update() throws Exception
    {
        //given
        Review existingReview = reviewRepository
                .findById(1L)
                .get();

        reviewRequestDto = ReviewDto.Request
                .builder()
                .id(existingReview.getId())
                .rate(2)
                .reviewMessage("Updated Message")
                .pillId(existingReview
                        .getPill()
                        .getId())
                .regDate(existingReview.getRegDate())
                .editDate(new Date())
                .build();

        setExpectedResponseDto(HttpStatus.OK.value(), "리뷰가 정상적으로 수정되었습니다.", null);

        //when
        ResultActions resultActions = excuteMockTest();

        //then
        validateServerResponse(resultActions);

        Review updatedReview = reviewRepository
                .findById(1L)
                .get();

        Review requestedReview = modelMapper.map(reviewRequestDto, Review.class);

        assertThat(updatedReview)
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(java.util.Date.class, java.sql.Timestamp.class)
                .ignoringFields("user", "pill")
                .isEqualTo(requestedReview);
    }


    @Test
    @DisplayName("[400][리뷰 작성] 한 유저가 리뷰 3개이상 작성")
    public void writeReviewMoreThanThreeTimes() throws Exception
    {
        //given
        setExpectedResponseDto(HttpStatus.BAD_REQUEST.value(), "리뷰는 약 하나당 3개까지만 등록 가능합니다. 추가 등록을 원하시는 경우 작성했던 리뷰를 삭제 후 등록해주세요!", null);

        reviewRequestDto = ReviewDto.Request
                .builder()
                .id(0L)
                .rate(3)
                .reviewMessage("리뷰 등록 메시지 TEST")
                .pillId(1L)
                .regDate(new Date())
                .editDate(new Date())
                .build();

        //when
        ResultActions resultActions = excuteMockTest();

        //then
        validateServerResponse(resultActions);
    }

    @ParameterizedTest
    @MethodSource("validateTestCases")
    @DisplayName("[400][리뷰 작성] 필드 유효성 검증")
    public void validationCheck(String field, Object value, String errorMessage) throws Exception
    {
        //given
        setExpectedResponseDto(HttpStatus.BAD_REQUEST.value(), errorMessage, null);

        reviewRequestDto = ReviewDto.Request
                .builder()
                .id(0L)
                .rate(3)
                .reviewMessage("리뷰 등록 메시지 TEST")
                .pillId(1L)
                .regDate(new Date())
                .editDate(new Date())
                .build();

        Field fieldToSet = reviewRequestDto
                .getClass()
                .getDeclaredField(field);
        fieldToSet.setAccessible(true);
        fieldToSet.set(reviewRequestDto, value);

        //when
        ResultActions resultActions = excuteMockTest();

        //then
        validateServerResponse(resultActions);
    }
}

