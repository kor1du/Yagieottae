package com.yagieottae_back_end.Service;

import com.yagieottae_back_end.Dto.PillDto;
import com.yagieottae_back_end.Dto.ResponseDto;
import com.yagieottae_back_end.Exception.CustomBadRequestException;
import com.yagieottae_back_end.Repository.PillRepository;
import com.yagieottae_back_end.Util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PillService
{
    private final PillRepository pillRepository;
    private final ModelMapper modelMapper;

    public ResponseDto getPill(String itemName, Pageable page)
    {
        ResponseDto returnValue;
        try
        {
            Page<PillDto.Response> pillDtoList = pillRepository
                    .findByItemName(itemName, page)
                    .orElseThrow(() -> new CustomBadRequestException("약 정보가 존재하지 않습니다."));

            if (pillDtoList.isEmpty())
            {
                throw new CustomBadRequestException("찾으시는 약이 존재하지 않습니다!");
            }

            returnValue = ResponseDto
                    .builder()
                    .httpStatus(HttpStatus.OK.value())
                    .message("조회 완료")
                    .body(JsonUtil.ObjectToJsonObject("pillList", pillDtoList))
                    .build();
        }
        catch (CustomBadRequestException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            log.error("findPill Unhandled Exception occured", e);
            throw new CustomBadRequestException("약 목록 조회에 실패하였습니다!");
        }

        return returnValue;
    }
}
