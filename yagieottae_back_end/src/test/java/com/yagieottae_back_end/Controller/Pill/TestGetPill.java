package com.yagieottae_back_end.Controller.Pill;

import com.fasterxml.jackson.databind.JsonNode;
import com.yagieottae_back_end.Component.TestBase;
import com.yagieottae_back_end.Dto.PillDto;
import com.yagieottae_back_end.Repository.PillRepository;
import com.yagieottae_back_end.Util.JsonUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class TestGetPill extends TestBase
{
    private final String url = "/pill/getPill";
    @Autowired
    private PillRepository pillRepository;

    //mockTest
    private ResultActions excuteMockTest() throws Exception
    {
        return mockMvc.perform(get(url).params(params));
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

        if (!responseJson
                .get("body")
                .isNull()) //body 검증
        {
            JsonNode responseBody = responseJson.get("body");

            if (responseBody.get("pillList") != null)
            {
                String serverResponseAlramList = responseBody
                        .get("pillList")
                        .toString();
                String expectedAlramList = expectedResponseDto
                        .getBody()
                        .get("pillList")
                        .toString();
                assertThat(serverResponseAlramList).isEqualTo(expectedAlramList);
            }
        }

        System.out.println("result \n" + responseJson.toPrettyString());
    }

    @Test
    @DisplayName("[200][약 조회]")
    public void getPill() throws Exception
    {
        //given
        String pillName = "베아제";
        Pageable page = PageRequest.of(0, 1);
        Page<PillDto.Response> pillDtoList = pillRepository
                .findByItemName(pillName, page)
                .get();

        setExpectedResponseDto(HttpStatus.OK.value(), "조회 완료", JsonUtil.ObjectToJsonObject("pillList", pillDtoList));

        params.add("itemName", "베아제");
        params.add("page", String.valueOf(page.getOffset()));
        params.add("size", String.valueOf(page.getPageSize()));

        //when
        ResultActions resultActions = excuteMockTest();

        //then
        validateServerResponse(resultActions);
    }

    @Test
    @DisplayName("[400][약 조회] 검색된 약 없음")
    public void notFound() throws Exception
    {
        //given
        params.add("itemName", "없는 약 이름");

        setExpectedResponseDto(HttpStatus.BAD_REQUEST.value(), "찾으시는 약이 존재하지 않습니다!", null);

        //when
        ResultActions resultActions = excuteMockTest();

        //then
        validateServerResponse(resultActions);
    }
}
