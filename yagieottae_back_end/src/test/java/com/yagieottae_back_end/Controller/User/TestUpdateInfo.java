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

import java.lang.reflect.Field;
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
public class TestUpdateInfo
{
    private UserDto.Info infoDto;
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
    private void doMockTest(ResponseDto expectedResponseDto) throws Exception
    {
        MvcResult result = mockMvc.perform(put("/user/updateInfo")
                                          .header("Authorization", "Bearer " + accessToken)
                                          .content(objectMapper.writeValueAsString(infoDto))
                                          .contentType(MediaType.APPLICATION_JSON))
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
        infoDto = UserDto.Info.builder()
                              .userId("user")
                              .nickname("수정된 별명")
                              .address("수정된 주소")
                              .addressDetail("수정된 상세주소")
                              .phone("010-5041-2235")
                              .profileImgPath("")
                              .build();
    }

    //Field 유효성 검사용 메서드
    private static Stream<Arguments> validationTestCases()
    {
        return Stream.of(
                Arguments.of("userId", null, "아이디를 입력해주세요!"),
                Arguments.of("userId", "", "아이디를 입력해주세요!"),
                Arguments.of("nickname", null, "별명을 입력해주세요!"),
                Arguments.of("nickname", "", "별명을 입력해주세요!"),
                Arguments.of("address", null, "주소를 입력해주세요!"),
                Arguments.of("address", "", "주소를 입력해주세요!"),
                Arguments.of("addressDetail", null, "상세 주소를 입력해주세요!"),
                Arguments.of("addressDetail", "", "상세 주소를 입력해주세요!"),
                Arguments.of("phone", null, "전화번호를 입력해주세요!"),
                Arguments.of("phone", "", "전화번호를 입력해주세요!"),
                Arguments.of("profileImgPath", null, "프로필 이미지 경로가 없습니다. 관리자에게 문의하세요!")
        );
    }

    @Test
    @DisplayName("[200][회원정보수정]")
    @Transactional
    public void test_UpdateInfo() throws Exception
    {
        expectedResponseDto = ResponseDto
                .builder()
                .httpStatus(HttpStatus.OK.value())
                .message("정보가 변경되었습니다.")
                .build();

        doMockTest(expectedResponseDto);
    }

    @Test
    @DisplayName("[400][회원정보수정] 별명 중복")
    public void test_UpdateInfo_NicknameAlreadyExists() throws Exception
    {
        infoDto.setNickname("별명");

        customBadRequestException = new CustomBadRequestException("입력하신 별명을 사용하는 유저가 존재합니다. 다른 별명을 사용해주세요!");
        expectedResponseDto = globalControllerExceptionHandler.handleBadRequestException(customBadRequestException).getBody();

        doMockTest(expectedResponseDto);
    }

    @ParameterizedTest
    @MethodSource("validationTestCases")
    @DisplayName("[400][회원정보수정] 필드 유효성 검증")
    public void test_UpdateInfo_ValidationCheck(String field, String value, String errorMessage) throws Exception
    {
        customBadRequestException = new CustomBadRequestException(errorMessage);
        expectedResponseDto = globalControllerExceptionHandler.handleBadRequestException(customBadRequestException).getBody();

        Field fieldToSet = infoDto.getClass().getDeclaredField(field);
        fieldToSet.setAccessible(true);
        fieldToSet.set(infoDto, value);

        doMockTest(expectedResponseDto);
    }

    @AfterEach
    public void afterEach(TestInfo testInfo)
    {
        log.info("{} 테스트 끝", testInfo.getDisplayName());
    }
}
