package com.yagieottae_back_end.Controller;

import com.yagieottae_back_end.Dto.ResponseDto;
import com.yagieottae_back_end.Dto.UserDto;
import com.yagieottae_back_end.Service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController
{
    private final UserService userService;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@Valid @RequestBody UserDto.Login userLoginDto)
    {
        return ResponseEntity.ok(userService.login(userLoginDto));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<ResponseDto> logout(@RequestParam String accessToken)
    {
        return ResponseEntity.ok(userService.logout(accessToken));
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<ResponseDto> signup(@Valid @RequestBody UserDto.Signup userSignupDto)
    {
        return ResponseEntity.ok(userService.signup(userSignupDto));
    }

    /**
     * 기능: 유저 정보 변경
     * 파라미터:
     * userInfo: 변경할 유저 정보
     * 작성자: kor1du
     * 수정자: kor1du
     * 반환 값: 유저 정보 변경 결과
     **/
    @PutMapping("/updateInfo")
    public ResponseEntity<ResponseDto> updateInfo(@Valid @RequestBody UserDto.Info userInfo)
    {
        return ResponseEntity.ok(userService.updateInfo(userInfo));
    }

    // 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<ResponseDto> reissue(@RequestParam String refreshToken)
    {
        return ResponseEntity.ok(userService.reissue(refreshToken));
    }
}
