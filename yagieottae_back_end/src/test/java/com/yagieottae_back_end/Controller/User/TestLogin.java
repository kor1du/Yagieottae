package com.yagieottae_back_end.Controller.User;

import com.fasterxml.jackson.databind.JsonNode;
import com.yagieottae_back_end.Component.TestBase;
import com.yagieottae_back_end.Configuration.TestMockConfig;
import com.yagieottae_back_end.Dto.ResponseDto;
import com.yagieottae_back_end.Dto.UserDto;
import com.yagieottae_back_end.Repository.UserRepository;
import com.yagieottae_back_end.Service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.lang.reflect.Field;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import({TestMockConfig.class})
@Slf4j
@Transactional
public class TestLogin extends TestBase
{
    private UserDto.Login loginDto;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @BeforeEach
    public void beforeEach(TestInfo testInfo)
    {
        // 인증 객체 생성
        Authentication auth = new UsernamePasswordAuthenticationToken("user", "1234");

        // SecurityContextHolder에 인증 객체 설정
        SecurityContextHolder.getContext().setAuthentication(auth);
        log.info("{} 테스트 시작", testInfo.getDisplayName());
        initializeTestData();
    }

    //    mockTest
    private void doMockTest(ResponseDto expectedResponseDto, ResultMatcher status) throws Exception
    {
        MvcResult result = mockMvc.perform(post("/user/login")
                                          .content(objectMapper.writeValueAsString(this.loginDto))
                                          .contentType(MediaType.APPLICATION_JSON))
                                  .andExpect(status)
                                  .andExpect(jsonPath("$.httpStatus").value(expectedResponseDto.getHttpStatus()))
                                  .andExpect(jsonPath("$.message").value(expectedResponseDto.getMessage()))
                                  .andReturn();

        String responseString = result.getResponse().getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseString);

        if (!responseJson.get("body").isNull()) //body 검증
        {
            JsonNode responseBody = responseJson.get("body");

            //userInfo 검증
            if (responseBody.get("userInfo") != null)
            {
                Assertions.assertEquals(responseBody.get("userInfo"), expectedResponseDto.getBody().get("userInfo"));
            }
            //jwtToken 미검증(테스트할때마다 토큰 값, 유효기간이 달라짐)
        }

        System.out.println("서버 응답: \n" + responseJson.toPrettyString());
    }

    //데이터 초기화
    private void initializeTestData()
    {
        loginDto = UserDto.Login
                .builder()
                .userId("user")
                .password("1234")
                .build();
    }

    //Field 유효성 검사용 메서드
    private static Stream<Arguments> validationTestCases()
    {
        return Stream.of(
                Arguments.of("userId", null, "아이디를 입력해주세요!"),
                Arguments.of("userId", "", "아이디를 입력해주세요!"),
                Arguments.of("password", null, "비밀번호를 입력해주세요!"),
                Arguments.of("password", "", "비밀번호를 입력해주세요!")
        );
    }

    @Test
    @DisplayName("[200][로그인]")
    public void test_Login() throws Exception
    {
        expectedResponseDto = userService.login(loginDto);

        doMockTest(expectedResponseDto, status().isOk());
    }

    @ParameterizedTest
    @MethodSource("validationTestCases")
    @DisplayName("[400][로그인] 필드 유효성 검증")
    public void test_Login_ValidationCheck(String field, String value, String errorMessage) throws Exception
    {
        setExpectedResponseDto(HttpStatus.BAD_REQUEST.value(), errorMessage, null);

        Field fieldToSet = loginDto.getClass().getDeclaredField(field);
        fieldToSet.setAccessible(true);
        fieldToSet.set(loginDto, value);

        doMockTest(expectedResponseDto, status().isBadRequest());
    }

    @Test
    @DisplayName("[400][로그인] 존재하지 않는 유저 ID로 로그인")
    public void test_Login_UserNotFound() throws Exception
    {
        setExpectedResponseDto(HttpStatus.BAD_REQUEST.value(), "해당 유저가 존재하지 않습니다!", null);

        this.loginDto.setUserId("notValidateUserId");

        doMockTest(expectedResponseDto, status().isBadRequest());
    }

    @Test
    @DisplayName("[400][로그인] 비밀번호 불일치")
    public void Test_Login_PasswordNotMatch() throws Exception
    {
        setExpectedResponseDto(HttpStatus.BAD_REQUEST.value(), "비밀번호가 일치하지 않습니다. 입력하신 내용을 다시 확인해주세요.", null);

        loginDto.setPassword("ThisIsWrongPassword");

        doMockTest(expectedResponseDto, status().isBadRequest());
    }

    @AfterEach
    public void afterEach(TestInfo testInfo)
    {
        log.info("{} 테스트 끝", testInfo.getDisplayName());
    }
}
