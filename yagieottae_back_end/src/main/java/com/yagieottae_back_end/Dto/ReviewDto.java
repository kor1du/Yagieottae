package com.yagieottae_back_end.Dto;

import com.yagieottae_back_end.Entity.Pill;
import com.yagieottae_back_end.Entity.User;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.joda.time.DateTime;

import java.util.Date;

public class ReviewDto
{
    public interface Response
    {
        Long getId();

        int getRate();

        String getReviewMessage();

        Date getEditDate();

        String getNickname();

        String getProfileImgPath();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class Request
    {
        private Long id; //PK
        @Max(value = 5, message = "별점은 5점까지만 입력 가능합니다!")
        @Min(value = 1, message = "별점은 1점이상으로만 입력 가능합니다!")
        private int rate; //평점
        @NotEmpty(message = "리뷰 메시지를 입력해주세요!")
        @Length(max = 100, message = "최대 100자까지만 입력이 가능합니다!")
        private String reviewMessage; //리뷰 메시지
        private Long pillId; //FK 약
        private Date regDate; //등록일
        private Date editDate; //수정일
    }
}
