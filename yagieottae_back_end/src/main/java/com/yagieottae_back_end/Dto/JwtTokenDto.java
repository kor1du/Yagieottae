package com.yagieottae_back_end.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
public class JwtTokenDto
{
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Date accessTokenExpireMs;
    private Date refreshTokenExpireMs;
}
