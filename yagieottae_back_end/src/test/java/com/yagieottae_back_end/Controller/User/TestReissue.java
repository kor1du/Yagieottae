package com.yagieottae_back_end.Controller.User;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yagieottae_back_end.Configuration.TestMockConfig;
import com.yagieottae_back_end.Dto.ResponseDto;
import com.yagieottae_back_end.Exception.CustomBadRequestException;
import com.yagieottae_back_end.Exception.GlobalControllerExceptionHandler;
import com.yagieottae_back_end.Dto.JwtTokenDto;
import com.yagieottae_back_end.Repository.UserRepository;
import com.yagieottae_back_end.Service.UserService;
import com.yagieottae_back_end.Util.TestTokenUtil;
import io.jsonwebtoken.JwtException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import({TestMockConfig.class})
@Slf4j
@Transactional
public class TestReissue
{
    private CustomBadRequestException customBadRequestException;
    private ResponseDto expectedResponseDto;
    private String accessToken;
    private String refreshToken;
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
    }

    //mockTest
    private void doMockTest(ResponseDto expectedResponseDto) throws Exception
    {
        MvcResult result = mockMvc.perform(post("/user/reissue")
                                          .param("refreshToken", refreshToken))
                                  .andExpect(jsonPath("$.httpStatus").value(expectedResponseDto.getHttpStatus()))
                                  .andExpect(jsonPath("$.message").value(expectedResponseDto.getMessage()))
                                  .andReturn();

        String responseString = result.getResponse().getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseString);

        System.out.println("서버 응답: \n" + responseJson.toPrettyString());
    }

    @Test
    @DisplayName("[200][토큰 재발급]")
    @Transactional
    public void test_Reissue() throws Exception
    {
        expectedResponseDto = userService.reissue(refreshToken);

        JwtTokenDto newJwtToken = objectMapper.treeToValue(expectedResponseDto.getBody().get("jwtTokenDto"), JwtTokenDto.class); //refreshToken을 새로 재발급된 토큰으로 교체
        refreshToken = newJwtToken.getRefreshToken();

        doMockTest(expectedResponseDto);
    }

    @Test
    @DisplayName("[401][토큰 재발급] 잘못된 refreshToken 전송")
    @Transactional
    public void test_Reissue_WrongRefreshToken() throws Exception
    {

        JwtException expectedJwtException = new JwtException("잘못된 JWT 시그니처");
        expectedResponseDto = globalControllerExceptionHandler.handleJwtException(expectedJwtException).getBody();

        refreshToken = "wrongToken";

        doMockTest(expectedResponseDto);
    }

    @AfterEach
    public void afterEach(TestInfo testInfo)
    {
        log.info("{} 테스트 끝", testInfo.getDisplayName());
    }
}
