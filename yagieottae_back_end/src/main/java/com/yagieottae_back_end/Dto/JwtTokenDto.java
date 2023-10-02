package com.yagieottae_back_end.Dto;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtTokenDto
{
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Date accessTokenExpireMs;
    private Date refreshTokenExpireMs;
}
