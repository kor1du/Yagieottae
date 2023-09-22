package com.yagieottae_back_end.Controller.Review;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.ResultMatcher;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import({TestMockConfig.class})
@Slf4j
public class TestSave
{
    private CustomBadRequestException customBadRequestException;
    private ResponseDto expectedResponseDto;
    private String itemName;
    private User user;
    private String accessToken;
    private ReviewDto.Request reviewRequestDto;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    PillRepository pillRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private GlobalControllerExceptionHandler globalControllerExceptionHandler;

    @BeforeEach
    public void beforeEach(TestInfo testInfo) throws Exception
    {
        log.info("{} 테스트 시작", testInfo.getDisplayName());
        initializeTestData();
    }

    //mockTest
    private void doMockTest(ResponseDto expectedResponseDto, ResultMatcher status) throws Exception
    {
        MvcResult result = mockMvc.perform(post("/review/save")
                                          .header("Authorization", "Bearer " + accessToken)
                                          .content(objectMapper.writeValueAsString(reviewRequestDto))
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
        User user = TestUserUtil.getTestUser();
        Pill pill = pillRepository.findById(1333L).get();

        reviewRequestDto = ReviewDto.Request.builder()
                                            .id(0L)
                                            .rate(5)
                                            .reviewMessage("리뷰 메시지")
                                            .pillId(pill.getId())
                                            .regDate(new Date())
                                            .editDate(new Date())
                                            .build();
    }

    //Field 유효성 검사용 메서드
    private static Stream<Arguments> validationTestCases()
    {
        return Stream.of(
                Arguments.of("rate", 0, "별점은 1점이상으로만 입력 가능합니다!"),
                Arguments.of("rate", 6, "별점은 5점까지만 입력 가능합니다!"),
                Arguments.of("reviewMessage", null, "리뷰 메시지를 입력해주세요!"),
                Arguments.of("reviewMessage", "", "리뷰 메시지를 입력해주세요!"),
                Arguments.of("reviewMessage", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec quis lacus vitae nibh pulvinar ornare.", "최대 100자까지만 입력이 가능합니다!"),
                Arguments.of("pillId", null, "약 정보가 부족합니다!")
        );
    }

    @Test
    @DisplayName("[200][리뷰 작성]")
//    @Transactional
    public void test_Save() throws Exception
    {
        expectedResponseDto = ResponseDto.builder()
                                         .httpStatus(HttpStatus.OK.value())
                                         .message("리뷰가 정상적으로 등록되었습니다.")
                                         .build();

        doMockTest(expectedResponseDto, status().isOk());
    }

    @Test
    @DisplayName("[200][리뷰 작성] 수정")
    @Transactional
    public void test_Save_Update() throws Exception
    {
        Review review = reviewRepository.findById(1L).get();

        reviewRequestDto = reviewRequestDto.toBuilder()
                                           .id(review.getId())
                                           .rate(2)
                                           .regDate(review.getRegDate())
                                           .reviewMessage("Updated")
                                           .editDate(new Date())
                                           .build();

        expectedResponseDto = ResponseDto.builder()
                                         .httpStatus(HttpStatus.OK.value())
                                         .message("리뷰가 정상적으로 수정되었습니다.")
                                         .build();

        doMockTest(expectedResponseDto, status().isOk());
    }

    @Test
    @DisplayName("[400][리뷰 작성] 잘못된 pillId")
    @Transactional
    public void test_Save_WrongPillId() throws Exception
    {
        reviewRequestDto.setPillId(-1L);

        expectedResponseDto = ResponseDto.builder()
                                         .httpStatus(HttpStatus.BAD_REQUEST.value())
                                         .message("약 정보가 존재하지 않습니다.")
                                         .build();

        doMockTest(expectedResponseDto, status().isBadRequest());
    }

    @Test
    @DisplayName("[400][리뷰 작성] 한 유저가 리뷰 3개이상 작성")
    @Transactional
    public void test_Save_WriteReviewMoreThanThreeTimes() throws Exception
    {
        for (int i = 0; i < 3; i++)
        {
            test_Save();
        }

        expectedResponseDto = ResponseDto.builder()
                                         .httpStatus(HttpStatus.BAD_REQUEST.value())
                                         .message("리뷰는 약 하나당 3개까지만 등록 가능합니다. 추가 등록을 원하시는 경우 작성했던 리뷰를 삭제 후 등록해주세요!")
                                         .build();

        doMockTest(expectedResponseDto, status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("validationTestCases")
    @DisplayName("[400][리뷰 작성] 필드 유효성 검증")
    public void test_Save_ValidationCheck(String field, Object value, String errorMessage) throws Exception
    {
        customBadRequestException = new CustomBadRequestException(errorMessage);
        expectedResponseDto = globalControllerExceptionHandler.handleBadRequestException(customBadRequestException).getBody();

        Field fieldToSet = reviewRequestDto.getClass().getDeclaredField(field);
        fieldToSet.setAccessible(true);
        fieldToSet.set(reviewRequestDto, value);

        doMockTest(expectedResponseDto, status().isBadRequest());
    }

    @AfterEach
    public void afterEach(TestInfo testInfo)
    {
        log.info("{} 테스트 끝", testInfo.getDisplayName());
    }
}

