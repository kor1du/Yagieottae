package com.yagieottae_back_end.Exception;

import com.yagieottae_back_end.Dto.ResponseDto;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerExceptionHandler
{
    /**
     * 기능: 기능 실행중 미리 예외처리해둔 Exception 핸들러
     * 파라미터:
     * exception: 예외 정보
     * 작성자: kor1du
     * 수정자: kor1du
     * 반환 값: 어떤 곳에서 예외가 발생했는지 사용자에게 메시지로 알려준다
     **/
    @ExceptionHandler(CustomBadRequestException.class)
    public ResponseEntity<ResponseDto> handleBadRequestException(CustomBadRequestException exception)
    {
        ResponseDto returnValue = ResponseDto.builder()
                                             .httpStatus(HttpStatus.BAD_REQUEST.value())
                                             .message(exception.getMessage())
                                             .build();

        return ResponseEntity.badRequest().body(returnValue);
    }

    /**
     * 기능: 기능 실행중 미리 예외처리해둔 Exception 핸들러
     * 파라미터:
     * exception: 예외 정보
     * 작성자: kor1du
     * 수정자: kor1du
     * 반환 값: 어떤 곳에서 예외가 발생했는지 사용자에게 메시지로 알려준다
     **/
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ResponseDto> handleJwtException(JwtException exception)
    {
        ResponseDto returnValue = ResponseDto.builder()
                                             .httpStatus(HttpStatus.UNAUTHORIZED.value())
                                             .message(exception.getMessage())
                                             .build();

        return ResponseEntity.badRequest().body(returnValue);
    }

    /**
     * 기능: 컨트롤러 호출할때 필수 인자 누락시 실행되는 Exception 핸들러
     * 파라미터:
     * exception: 예외 정보
     * 작성자: kor1du
     * 수정자: kor1du
     * 반환 값: 어떤 인자가 누락됐는지 사용자에게 메시지로 알려준다
     **/
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception)
    {
        ResponseDto returnValue = ResponseDto.builder()
                                             .httpStatus(HttpStatus.BAD_REQUEST.value())
                                             .message(exception.getFieldError().getDefaultMessage())
                                             .build();

        return ResponseEntity.badRequest().body(returnValue);
    }
}
