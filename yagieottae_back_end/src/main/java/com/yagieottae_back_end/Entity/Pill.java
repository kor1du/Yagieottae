package com.yagieottae_back_end.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "pill")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Pill
{
    //PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //약 고유번호
    @NotNull
    private Long itemSeq;

    //약 이름
    @NotNull
    private String itemName;

    //약 사진
    private String imagePath;

    //업체명
    @NotNull
    private String entpName;

    //주 성분
    @NotNull
    private String mainIngredient;

    //효능
    @NotNull
    private String efcyQesitm;

    //사용법
    private String useMethodQesitm;

    //필수 주의사항
    @NotNull
    private String atpnWarnQesitm;

    //주의사항
    @NotNull
    private String atpnQesitm;

    //복용시 주의해야할 약 또는 음식
    @NotNull
    private String intrcQesitm;

    //부작용
    @NotNull
    private String seQesitm;

    //보관법
    @NotNull
    private String depositMethodQesitm;

    //공개일자
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date regDate;

    //수정일자
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date editDate;
}
