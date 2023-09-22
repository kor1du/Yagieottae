package com.yagieottae_back_end.Dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

public class UserDto
{
    //로그인시 사용하는 DTO
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Login
    {
        @NotEmpty(message = "아이디를 입력해주세요!")
        private String userId; //아이디
        @NotEmpty(message = "비밀번호를 입력해주세요!")
        private String password; //비밀번호
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Logout
    {
        @NotEmpty(message = "로그아웃할 회원 정보가 존재하지 않습니다!")
        public String accessToken;
    }

    //유저 정보
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class Info
    {
        @NotEmpty(message = "아이디를 입력해주세요!")
        private String userId; //아이디
        @NotEmpty(message = "별명을 입력해주세요!")
        private String nickname; //별명
        @NotEmpty(message = "주소를 입력해주세요!")
        private String address; //주소
        @NotEmpty(message = "상세 주소를 입력해주세요!")
        private String addressDetail; // 상세주소
        @NotEmpty(message = "전화번호를 입력해주세요!")
        private String phone; // 전화번호
        @NotNull(message = "프로필 이미지 경로가 없습니다. 관리자에게 문의하세요!")
        private String profileImgPath; //프로필 사진 저장 경로
    }

    // 회원가입 전용 DTO
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Signup
    {
        @NotEmpty(message = "아이디를 입력해주세요!")
        private String userId; //아이디
        @NotEmpty(message = "비밀번호를 입력해주세요!")
        private String password; // 비밀번호
        @NotEmpty(message = "확인용 비밀번호를 입력해주세요!")
        private String passwordConfirm; //비밀번호 확인
        @NotEmpty(message = "별명을 입력해주세요!")
        private String nickname; //별명
        @NotEmpty(message = "주소를 입력해주세요!")
        private String address; //주소
        @NotEmpty(message = "상세 주소를 입력해주세요!")
        private String addressDetail; // 상세주소
        @NotEmpty(message = "전화번호를 입력해주세요!")
        private String phone; // 전화번호
        private String profileImgPath; //프로필 사진 저장 경로
    }
}
