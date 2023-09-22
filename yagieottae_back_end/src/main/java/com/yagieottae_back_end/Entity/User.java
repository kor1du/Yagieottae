package com.yagieottae_back_end.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yagieottae_back_end.Enum.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "user")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class User
{
    //PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //아이디
    @NotNull
    private String userId;

    //비밀번호
    @NotNull
    private String password;

    //별명
    @NotNull
    private String nickname;

    //주소
    @NotNull
    private String address;

    //상세 주소
    @NotNull
    private String addressDetail;

    //전화번호
    @NotNull
    private String phone;

    //가입일자
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
    private Date regDate;

    //프로필 사진 저장 경로
    private String profileImgPath;

    //권한
    @Enumerated(EnumType.STRING)
    private Role role;
}
