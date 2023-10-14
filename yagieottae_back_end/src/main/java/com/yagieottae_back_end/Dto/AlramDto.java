package com.yagieottae_back_end.Dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AlramDto
{
    public interface Response
    {
        Long getId(); //PK

        String getAlramTime(); // 알람 시간

        String getDays(); // 요일 반복 월, 화, 수, 목, 금, 토, 일

        Boolean getBeforeMeal(); // true:식전 복용 false: 식후 복용

        int getDosingTime(); // 복용 시간

        PillDto.Response getPill(); // 약 정보
    }

    //Alram 생성 DTO
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request
    {
        private Long id; //PK
        @NotEmpty(message = "알람 시간을 입력해주세요!")
        private String alramTime; // 알람 시간
        @NotEmpty(message = "요일을 선택해주세요!")
        private String days; // 요일 반복 월, 화, 수, 목, 금, 토, 일
        @NotNull(message = "식전 / 식후 복용을 선택해주세요!")
        private Boolean beforeMeal; // true:식전 복용 false: 식후 복용
        @Max(value = 60, message = "복용시간은 60분 내외로만 설정 가능합니다!")
        @Positive(message = "복용시간은 0보다 큰 숫자여야 합니다!")
        private int dosingTime; // 복용 시간
        @NotNull(message = "약 정보가 존재하지 않습니다!")
        private Long pillId; // (FK) 약 PK키
    }
}
