package com.yagieottae_back_end.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestDto
{
    private Long id;
    private int rate; //평점
    private String reviewMessage; //리뷰 메시지
    private Date editDate; //수정일
    private String nickname; //유저 별명
    private String profileImgPath; //유저 프로필 이미지
}
