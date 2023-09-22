package com.yagieottae_back_end.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "alram")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Alram
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK키

    @NotNull
    private String alramTime; // 알람 예정 시간

    @NotNull
    private String days; // 요일 반복 1:월, 2:화, 3:수, 4:목, 5:금, 6:토, 7:일

    @NotNull
    private Boolean beforeMeal; // true:식전 복용 false: 식후 복용

    @NotNull
    private Long dosingTime; //복용시간

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date regDate; // 칼럼 생성일자

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date editDate; // 칼럼 수정일자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user; // (FK) 유저 정보

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pill_id", nullable = false)
    private Pill pill; // (FK) 약 정보
}