package com.yagieottae_back_end.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yagieottae_back_end.Dto.PillDto;
import com.yagieottae_back_end.Dto.ResponseDto;
import com.yagieottae_back_end.Dto.UserDto;
import com.yagieottae_back_end.Entity.Pill;
import com.yagieottae_back_end.Exception.CustomBadRequestException;
import com.yagieottae_back_end.Repository.PillRepository;
import com.yagieottae_back_end.Util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PillService
{
    private final PillRepository pillRepository;
    private final ModelMapper modelMapper;

    public ResponseDto findPill(String itemName)
    {
        ResponseDto returnValue = new ResponseDto();
        try
        {
            List<PillDto.Response> pillDtoList;

            List<Pill> pillList = pillRepository.findByItemNameContains(itemName).get();

            if (pillList.isEmpty())
            {
                throw new CustomBadRequestException("찾으시는 약이 존재하지 않습니다!");
            } else
            {
                pillDtoList = pillList
                        .stream()
                        .map(pill ->
                                PillDto.Response.builder()
                                                .id(pill.getId())
                                                .itemSeq(pill.getItemSeq())
                                                .itemName(pill.getItemName())
                                                .imagePath(pill.getImagePath())
                                                .entpName(pill.getEntpName())
                                                .mainIngredient(pill.getMainIngredient())
                                                .efcyQesitm(pill.getEfcyQesitm())
                                                .useMethodQesitm(pill.getUseMethodQesitm())
                                                .atpnWarnQesitm(pill.getAtpnWarnQesitm())
                                                .atpnQesitm(pill.getAtpnQesitm())
                                                .intrcQesitm(pill.getIntrcQesitm())
                                                .seQesitm(pill.getSeQesitm())
                                                .depositMethodQesitm(pill.getDepositMethodQesitm())
                                                .build()).collect(Collectors.toList());
            }

            returnValue = ResponseDto.builder()
                                     .httpStatus(HttpStatus.OK.value())
                                     .message("조회 완료")
                                     .body(JsonUtil.ObjectToJsonObject("pillList", pillDtoList))
                                     .build();
        } catch (CustomBadRequestException e)
        {
            throw e;
        } catch (Exception e)
        {
            log.error("findPill Unhandled Exception occured", e);
            throw new CustomBadRequestException("약 목록 조회에 실패하였습니다!");
        }

        return returnValue;
    }
}
