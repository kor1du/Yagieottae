package com.yagieottae_back_end.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "review")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Review
{
    //PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //PK

    @NotNull
    private int rate; //평점

    @NotNull
    @Column(length = 100)
    private String reviewMessage; //리뷰 메시지 최대 100자 제한

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user; //FK 유저

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pill_id", nullable = false)
    @JsonIgnore
    private Pill pill; //FK 약

    @NotNull
    private Date regDate; //등록일

    @NotNull
    private Date editDate; //수정일
}
