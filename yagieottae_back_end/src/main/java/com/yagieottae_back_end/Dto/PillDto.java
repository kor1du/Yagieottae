package com.yagieottae_back_end.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PillDto
{
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response
    {
        private Long id; //PK키
        private Long itemSeq; //약 품목번호(ID)
        private String itemName; //약 이름
        private String imagePath; //약 이미지 경로
        private String entpName; //업체명
        private String mainIngredient; //주 성분
        private String efcyQesitm; //효능
        private String useMethodQesitm; //사용법
        private String atpnWarnQesitm; //필수 주의사항
        private String atpnQesitm; //주의사항
        private String intrcQesitm; //약 복용시 주의해야 할 약 또는 음식
        private String seQesitm; //부작용
        private String depositMethodQesitm; //보관법
    }
}
