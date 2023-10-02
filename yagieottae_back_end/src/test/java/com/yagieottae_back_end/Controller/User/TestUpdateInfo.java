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
import org.springframework.test.web.servlet.ResultActions;

import java.lang.reflect.Field;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

public class TestUpdateInfo extends TestBase
{
    private UserDto.Info infoDto;
    @Autowired
    private UserService userService;

    //mockTest
    private ResultActions excuteMockTest() throws Exception
    {
        return mockMvc.perform(put("/user/updateInfo")
                .header("Authorization", "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(infoDto))
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

        //@formatter:off
        if (!responseJson.get("body").isNull())
        {
            JsonNode serverResponedUserInfoJson = responseJson.get("body").get("userInfo");
            UserDto.Info serverResponsedUserInfo = UserDto.Info
                    .builder()
                    .nickname(serverResponedUserInfoJson.get("nickname").textValue())
                    .address(serverResponedUserInfoJson.get("address").textValue())
                    .addressDetail(serverResponedUserInfoJson.get("addressDetail").textValue())
                    .phone(serverResponedUserInfoJson.get("phone").textValue())
                    .profileImgPath(serverResponedUserInfoJson.get("profileImgPath").textValue())
                    .build();

            assertThat(infoDto)
                    .isEqualTo(serverResponsedUserInfo);
        }
        //@formatter:on

        System.out.println("서버 응답: \n" + responseJson.toPrettyString());
    }

    //데이터 초기화
    private void setDefaultInfoDto() throws Exception
    {
        infoDto = UserDto.Info
                .builder()
                .nickname("수정된 별명")
                .address("수정된 주소")
                .addressDetail("수정된 상세주소")
                .phone("010-5041-2235")
                .profileImgPath("")
                .build();
    }

    //Field 유효성 검사용 메서드
    private static Stream<Arguments> validateTestCases()
    {
        //@formatter:off
        return Stream.of(
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
        //@formatter:on
    }

    @Test
    @DisplayName("[200][회원정보수정]")
    public void updateInfo() throws Exception
    {
        //given
        setDefaultInfoDto();

        expectedResponseDto = userService.updateInfo(infoDto);

        //when
        ResultActions resultActions = excuteMockTest();

        //then
        validateServerResponse(resultActions);
    }

    @Test
    @DisplayName("[400][회원정보수정] 별명 중복")
    public void nicknameAlreadyExists() throws Exception
    {
        //given
        setDefaultInfoDto();

        infoDto.setNickname("관리자");

        setExpectedResponseDto(HttpStatus.BAD_REQUEST.value(), "입력하신 별명을 사용하는 유저가 존재합니다. 다른 별명을 사용해주세요!", null);

        //when
        ResultActions resultActions = excuteMockTest();

        //then
        validateServerResponse(resultActions);
    }

    @ParameterizedTest
    @MethodSource("validateTestCases")
    @DisplayName("[400][회원정보수정] 필드 유효성 검증")
    public void validationCheck(String field, String value, String errorMessage) throws Exception
    {
        //given
        setDefaultInfoDto();

        setExpectedResponseDto(HttpStatus.BAD_REQUEST.value(), errorMessage, null);

        Field fieldToSet = infoDto
                .getClass()
                .getDeclaredField(field);
        fieldToSet.setAccessible(true);
        fieldToSet.set(infoDto, value);

        //when
        ResultActions resultActions = excuteMockTest();

        //then
        validateServerResponse(resultActions);
    }
}
