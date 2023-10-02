package com.yagieottae_back_end.Dto;

public class PillDto
{
    public interface Response
    {
        Long getId(); //PK키
        Long getItemSeq(); //약 품목번호(ID)
        String getItemName(); //약 이름
        String getImagePath(); //약 이미지 경로
        String getEntpName(); //업체명
        String getMainIngredient(); //주 성분
        String getEfcyQesitm(); //효능
        String getUseMethodQesitm(); //사용법
        String getAtpnWarnQesitm(); //필수 주의사항
        String getAtpnQesitm(); //주의사항
        String getIntrcQesitm(); //약 복용시 주의해야 할 약 또는 음식
        String getSeQesitm(); //부작용
        String getDepositMethodQesitm(); //보관법

    }

//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @Builder
//    public static class Response
//    {
//        private Long id; //PK키
//        private Long itemSeq; //약 품목번호(ID)
//        private String itemName; //약 이름
//        private String imagePath; //약 이미지 경로
//        private String entpName; //업체명
//        private String mainIngredient; //주 성분
//        private String efcyQesitm; //효능
//        private String useMethodQesitm; //사용법
//        private String atpnWarnQesitm; //필수 주의사항
//        private String atpnQesitm; //주의사항
//        private String intrcQesitm; //약 복용시 주의해야 할 약 또는 음식
//        private String seQesitm; //부작용
//        private String depositMethodQesitm; //보관법
//    }
}
