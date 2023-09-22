package com.yagieottae_back_end.Dto;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDto
{
    private int httpStatus; //httpStatus Code
    private String message; //반환 메시지 ex) 저장에 성공했습니다, 저장에 실패했습니다, ...etc
    private ObjectNode body; //반환 데이터 ex)Json으로 변환된 데이터
}
