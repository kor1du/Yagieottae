package com.yagieottae_back_end.Controller.User;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yagieottae_back_end.Component.TestBase;
import com.yagieottae_back_end.Configuration.TestMockConfig;
import com.yagieottae_back_end.Dto.ResponseDto;
import com.yagieottae_back_end.Dto.UserDto;
import com.yagieottae_back_end.Exception.CustomBadRequestException;
import com.yagieottae_back_end.Exception.GlobalControllerExceptionHandler;
import com.yagieottae_back_end.Repository.UserRepository;
import com.yagieottae_back_end.Service.UserService;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.lang.reflect.Field;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

public class TestSignup extends TestBase
{
    private final String url = "/user/signup";
    private UserDto.Signup signupDto;
    @Autowired
    private UserRepository userRepository;

    //mockTest
    private ResultActions excuteMockTest() throws Exception
    {
        return mockMvc.perform(post("/user/signup")
                .content(objectMapper.writeValueAsString(signupDto))
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

        System.out.println("서버 응답: \n" + responseJson.toPrettyString());
    }

    //데이터 초기화
    private void setDefaultSignupDto()
    {
        signupDto = UserDto.Signup
                .builder()
                .userId("testUserId")
                .password("testPassword")
                .passwordConfirm("testPassword")
                .nickname("testNickname")
                .address("testAddress")
                .addressDetail("testAddressDetail")
                .phone("010-1234-1234")
                .build();
    }

    //Field 유효성 검사용 메서드
    private static Stream<Arguments> validateTestCases()
    {
        //@formatter:off
        return Stream.of(
                Arguments.of("userId", null, "아이디를 입력해주세요!"),
                Arguments.of("userId", "", "아이디를 입력해주세요!"),
                Arguments.of("password", null, "비밀번호를 입력해주세요!"),
                Arguments.of("password", "", "비밀번호를 입력해주세요!"),
                Arguments.of("passwordConfirm", null, "확인용 비밀번호를 입력해주세요!"),
                Arguments.of("passwordConfirm", "", "확인용 비밀번호를 입력해주세요!"),
                Arguments.of("nickname", null, "별명을 입력해주세요!"),
                Arguments.of("nickname", "", "별명을 입력해주세요!"),
                Arguments.of("address", null, "주소를 입력해주세요!"),
                Arguments.of("address", "", "주소를 입력해주세요!"),
                Arguments.of("addressDetail", null, "상세 주소를 입력해주세요!"),
                Arguments.of("addressDetail", "", "상세 주소를 입력해주세요!"),
                Arguments.of("phone", null, "전화번호를 입력해주세요!"),
                Arguments.of("phone", "", "전화번호를 입력해주세요!"));
        //@formatter:on
    }

    @Test
    @DisplayName("[200][회원가입]")
    public void signup() throws Exception
    {
        //given
        setExpectedResponseDto(HttpStatus.OK.value(), "회원가입에 성공하였습니다. 약이어때의 회원이 되신걸 환영합니다!", null);

        setDefaultSignupDto();

        //when
        ResultActions resultActions = excuteMockTest();

        //then
        validateServerResponse(resultActions);
        userRepository
                .findByUserId(signupDto.getUserId())
                .orElseThrow(() -> new CustomBadRequestException("회원가입한 유저가 존재하지 않습니다."));
    }

    @Test
    @DisplayName("[400][회원가입] 입력한 비밀번호와 확인용 비밀번호가 불일치")
    public void passwordNotEqualsPasswordConfirm() throws Exception
    {
        //given
        setDefaultSignupDto();

        signupDto.setPasswordConfirm("notEqualPassword");

        setExpectedResponseDto(HttpStatus.BAD_REQUEST.value(), "비밀번호와 확인용 비밀번호가 다릅니다!", null);

        //when
        ResultActions resultActions = excuteMockTest();

        //then
        validateServerResponse(resultActions);
    }

    @Test
    @DisplayName("[400][회원가입] 아이디 중복")
    public void userIdAlreadyExists() throws Exception
    {
        //given
        setDefaultSignupDto();

        signupDto.setUserId("user");

        setExpectedResponseDto(HttpStatus.BAD_REQUEST.value(), "입력하신 아이디와 중복되는 아이디가 존재합니다. 다른 아이디를 사용해주세요.", null);

        //when
        ResultActions resultActions = excuteMockTest();

        //then
        validateServerResponse(resultActions);
    }

    @Test
    @DisplayName("[400][회원가입] 별명 중복")
    public void nicknameAlreadyExists() throws Exception
    {
        //given
        setDefaultSignupDto();

        signupDto.setNickname("유저");

        setExpectedResponseDto(HttpStatus.BAD_REQUEST.value(), "입력하신 별명을 사용하는 유저가 존재합니다. 다른 별명을 사용해주세요!", null);

        //when
        ResultActions resultActions = excuteMockTest();

        //then
        validateServerResponse(resultActions);
    }

    @ParameterizedTest
    @MethodSource("validateTestCases")
    @DisplayName("[400][회원가입] 필드 유효성 검증")
    public void test_Signup_ValidationCheck(String field, String value, String errorMessage) throws Exception
    {
        //given
        setDefaultSignupDto();
        setExpectedResponseDto(HttpStatus.BAD_REQUEST.value(), errorMessage, null);

        Field fieldToSet = signupDto
                .getClass()
                .getDeclaredField(field);
        fieldToSet.setAccessible(true);
        fieldToSet.set(signupDto, value);

        //when
        ResultActions resultActions = excuteMockTest();

        //then
        validateServerResponse(resultActions);
    }
}
