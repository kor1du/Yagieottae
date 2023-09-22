package com.yagieottae_back_end.Controller.User;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yagieottae_back_end.Configuration.TestMockConfig;
import com.yagieottae_back_end.Dto.ResponseDto;
import com.yagieottae_back_end.Dto.UserDto;
import com.yagieottae_back_end.Exception.CustomBadRequestException;
import com.yagieottae_back_end.Exception.GlobalControllerExceptionHandler;
import com.yagieottae_back_end.Repository.UserRepository;
import com.yagieottae_back_end.Service.UserService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import({TestMockConfig.class})
@Slf4j
@Transactional
public class TestLogout
{
    private UserDto.Logout logoutDto;
    private CustomBadRequestException customBadRequestException;
    private ResponseDto expectedResponseDto;
    private String accessToken;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
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
        MvcResult result = mockMvc.perform(post("/user/logout")
                                          .content(objectMapper.writeValueAsString(logoutDto))
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
        logoutDto = UserDto.Logout.builder()
                                  .accessToken(accessToken)
                                  .build();
    }

    //Field 유효성 검사용 메서드
    private static Stream<Arguments> validationTestCases()
    {
        return Stream.of(
                Arguments.of("accessToken", null, "로그아웃할 회원 정보가 존재하지 않습니다!"),
                Arguments.of("accessToken", "", "로그아웃할 회원 정보가 존재하지 않습니다!")
        );
    }

    @Test
    @DisplayName("[200][로그아웃]")
    public void test_Logout() throws Exception
    {
        expectedResponseDto = userService.logout(logoutDto);

        doMockTest(expectedResponseDto, status().isOk());
    }

    @ParameterizedTest
    @MethodSource("validationTestCases")
    @DisplayName("[400][로그아웃] 필드 유효성 검증")
    public void test_Logout_ValidationCheck(String field, String value, String errorMessage) throws Exception
    {
        customBadRequestException = new CustomBadRequestException(errorMessage);
        expectedResponseDto = globalControllerExceptionHandler.handleBadRequestException(customBadRequestException).getBody();
        logoutDto.setAccessToken(value);

        doMockTest(expectedResponseDto, status().isBadRequest());
    }

    @Test
    @DisplayName("[400][로그아웃] 로그아웃된 토큰으로 사용자 인증 시도")
    public void test_Logout_ReUseLogouttedToken() throws Exception
    {
        test_Logout(); //로그아웃

        //로그아웃된 토큰으로 유저가 설정해둔 알람 정보 조회 시도
        expectedResponseDto = ResponseDto.builder()
                                         .httpStatus(HttpStatus.UNAUTHORIZED.value())
                                         .message("로그아웃된 토큰입니다!")
                                         .build();

        MvcResult result = mockMvc.perform(get("/alram/findAlrams")
                                          .header("Authorization", "Bearer " + accessToken)
                                          .content(objectMapper.writeValueAsString(logoutDto))
                                          .contentType(MediaType.APPLICATION_JSON))
                                  .andExpect(status().isUnauthorized())
                                  .andExpect(jsonPath("$.httpStatus").value(expectedResponseDto.getHttpStatus()))
                                  .andExpect(jsonPath("$.message").value(expectedResponseDto.getMessage()))
                                  .andReturn();

        String responseString = result.getResponse().getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseString);

        System.out.println("서버 응답: \n" + responseJson.toPrettyString());
    }

    @AfterEach
    public void afterEach(TestInfo testInfo)
    {
        log.info("{} 테스트 끝", testInfo.getDisplayName());
    }
}
