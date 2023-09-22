package com.yagieottae_back_end.Exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yagieottae_back_end.Dto.ResponseDto;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class GlobalFilterExceptionHandler extends OncePerRequestFilter
{
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException
    {
        try
        {
            chain.doFilter(request, response);
        } catch (JwtException ex)
        {
            setErrorResponse(response, ex, HttpStatus.UNAUTHORIZED);
        } catch (Exception ex)
        {
            setErrorResponse(response, ex, HttpStatus.BAD_REQUEST);
        }
    }

    public void setErrorResponse(HttpServletResponse res, Throwable ex, HttpStatus status) throws IOException
    {
        ResponseDto exceptionResponseDto = ResponseDto.builder()
                                                      .httpStatus(status.value())
                                                      .message(ex.getMessage()) //에러 메시지 설정
                                                      .build();

        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(status.value());
        res.getWriter().print(objectMapper.valueToTree(exceptionResponseDto));
    }
}
