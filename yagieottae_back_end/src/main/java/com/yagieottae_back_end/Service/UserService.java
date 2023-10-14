package com.yagieottae_back_end.Service;

import com.mysql.cj.Session;
import com.yagieottae_back_end.Dto.ResponseDto;
import com.yagieottae_back_end.Dto.UserDto;
import com.yagieottae_back_end.Entity.User;
import com.yagieottae_back_end.Enum.Role;
import com.yagieottae_back_end.Exception.CustomBadRequestException;
import com.yagieottae_back_end.Dto.JwtTokenDto;
import com.yagieottae_back_end.Jwt.JwtTokenService;
import com.yagieottae_back_end.Repository.UserRepository;
import com.yagieottae_back_end.Util.JsonUtil;
import com.yagieottae_back_end.Util.SessionUtil;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService
{
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenService jwtTokenService;
    private final RedisTemplate<String, String> redisTemplate;
    private final ModelMapper modelMapper;

    //로그인
    public ResponseDto login(UserDto.Login userLoginDto)
    {
        try
        {
            User user = userRepository
                    .findByUserId(userLoginDto.getUserId())
                    .orElseThrow(() -> new CustomBadRequestException("해당 유저가 존재하지 않습니다!")); //유저ID로 유저 검색

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userLoginDto.getUserId(), userLoginDto.getPassword()); //id와 password로 AuthenticationToken객체 생성

            //비밀번호 불일치시 JwtCustomAuthenticationProvider.class에 override된 authenticate 메서드에서 CustomBadRequest 오류 발생
            Authentication authentication = authenticationManagerBuilder
                    .getObject()
                    .authenticate(authenticationToken); //authenticationToken 객체를 토대로 authentication 객체 생성

            JwtTokenDto jwtTokenDTO = jwtTokenService.generateToken(authentication); //authentication 객체로 jwtTokenDTO 객체 생성

            HashMap<String, Object> resultMap = new HashMap<>(); // 토큰, 유저 정보를 담을 HashMap 객체 선언
            resultMap.put("jwtToken", jwtTokenDTO); //토큰 정보 저장
            resultMap.put("userInfo", modelMapper.map(user, UserDto.Info.class)); //유저 정보 저장

            return ResponseDto
                    .builder()
                    .httpStatus(HttpStatus.OK.value())
                    .message("로그인에 성공하였습니다.")
                    .body(JsonUtil.ObjectToJsonObject(resultMap))
                    .build();
        }
        catch (CustomBadRequestException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            log.error("login Unhandled Exception occured", e);
            throw new CustomBadRequestException("로그인에 실패하였습니다!");
        }
    }

    //토큰 재발급
    public ResponseDto reissue(String refreshToken)
    {
        try
        {
            Authentication authentication = getAuthenticationFromRedisByRefreshToken(refreshToken); //redis의 refreshToken정보와 클라이언트가 전송한 refreshToken의 정보를 비교해서 일치하면 refreshToken의 정보로 authentication 변수 초기화

            JwtTokenDto jwtTokenDTO = jwtTokenService.generateToken(authentication); //authentication의 유저 정보로 새로운 JWT 토큰 생성

            redisTemplate
                    .opsForValue()
                    .set("RT:" + authentication.getName(), jwtTokenDTO.getRefreshToken(), jwtTokenDTO
                            .getRefreshTokenExpireMs()
                            .getTime(), TimeUnit.MILLISECONDS); //새로 생성된 토큰정보에서 refreshToken 정보를 redis에 저장

            return ResponseDto
                    .builder()
                    .httpStatus(HttpStatus.OK.value())
                    .message("토큰이 재발급 되었습니다.")
                    .body(JsonUtil.ObjectToJsonObject("jwtTokenDto", jwtTokenDTO))
                    .build();
        }
        catch (Exception e)
        {
            log.error("unhandled Exception occured", e);
            throw new CustomBadRequestException("토큰 재발급중 오류가 발생했습니다!");
        }
    }

    /**
     * 기능: 회원가입 결과 반환
     * 파라미터:
     * userSignupDto: 회원가입 정보
     * 작성자: kor1du
     * 수정자: kor1du
     * 반환 값: 회원가입 결과 반환
     **/
    public ResponseDto signup(UserDto.Signup userSignupDto)
    {
        ResponseDto returnValue;

        try
        {
            if (!userSignupDto
                    .getPassword()
                    .equals(userSignupDto.getPasswordConfirm()))
            {
                throw new CustomBadRequestException("비밀번호와 확인용 비밀번호가 다릅니다!");
            }

            if (userRepository
                    .findByUserId(userSignupDto.getUserId())
                    .isPresent()) // 동일한 아이디로 이미 가입한 유저가 존재한다면
            {
                throw new CustomBadRequestException("입력하신 아이디와 중복되는 아이디가 존재합니다. 다른 아이디를 사용해주세요.");
            }

            if (userRepository
                    .findByNickname(userSignupDto.getNickname())
                    .isPresent()) //별명 중복체크
            {
                throw new CustomBadRequestException("입력하신 별명을 사용하는 유저가 존재합니다. 다른 별명을 사용해주세요!");
            }

            String encodedPassword = passwordEncoder.encode(userSignupDto.getPassword()); //DB에 저장할 유저 정보의 비밀번호 암호화
            userSignupDto.setPassword(encodedPassword); //회원가입할 데이터의 평문 비밀번호를 암호화된 비밀번호로 교체

            User user = User.builder() //DB에 저장할 User 객체 생성
                    .userId(userSignupDto.getUserId())
                    .password(userSignupDto.getPassword())
                    .nickname(userSignupDto.getNickname())
                    .address(userSignupDto.getAddress())
                    .addressDetail(userSignupDto.getAddressDetail())
                    .phone(userSignupDto.getPhone())
                    .profileImgPath("https://yagieottae-s3-bucket.s3.ap-northeast-2.amazonaws.com/userDefaultProfileImg.png")
                    .regDate(new Date())
                    .role(Role.USER)
                    .build();

            userRepository.save(user); //DB 저장

            returnValue = ResponseDto
                    .builder()
                    .httpStatus(HttpStatus.OK.value())
                    .message("회원가입에 성공하였습니다. 약이어때의 회원이 되신걸 환영합니다!")
                    .build();
        }
        catch (CustomBadRequestException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            log.error("signup Unhandled Exception occured", e);
            throw new CustomBadRequestException("회원가입에 실패하였습니다!");
        }

        return returnValue;
    }

    /**
     * 기능: 사용자가 전송한 refreshToken의 정보와 redis에 저장되어 있는 refreshToken을 비교해서 일치하면 Authentication 객체 반환
     * 파라미터:
     * refreshToken: 사용자가 전송한 refreshToken 정보가 담긴 문자열
     * 작성자: kor1du
     * 수정자: kor1du
     * 반환 값: 유저정보가 담긴 Authentication
     **/
    public Authentication getAuthenticationFromRedisByRefreshToken(String refreshToken)
    {
        Authentication returnValue;

        if (!jwtTokenService.validateToken(refreshToken)) //파라미터로 넘어온 refreshToken가 유효하지 않다면
        {
            throw new CustomBadRequestException("Refresh Token 정보가 유효하지 않습니다.");
        }

        returnValue = jwtTokenService.getAuthentication(refreshToken); //refreshToken의 유저정보를 파싱하여 해당 유저 정보로 Authentication 변수 초기화

        String redisRefreshToken = redisTemplate
                .opsForValue()
                .get("RT:" + returnValue.getName()); //authentication 변수에 저장되어있는 유저이름으로 redis에 저장된 값이 있는지 찾고 redis에 저장되어 있는 RefreshToken정보로 변수 초기화

        if (!refreshToken.equals(redisRefreshToken)) //사용자가 전송한 refreshToken 정보와 redis에 저장되어 있는 refreshToken 정보가 일치하지 않는다면
        {
            throw new JwtException("Refresh Token 정보가 유효하지 않습니다.");
        }

        return returnValue; //유저정보가 담긴 Authentication 객체 리턴
    }

    //로그아웃
    public ResponseDto logout(String accessToken)
    {
        try
        {
            Authentication authentication = jwtTokenService.getAuthentication(accessToken); //logoutDto.accessToken의 정보로 Authentication 객체 생성

            Long tokenExpiration = jwtTokenService.getTokenExpirationDate(accessToken); //로그아웃 요청한 logoutDto.accessToken의 유효기간

            redisTemplate.delete("RT:" + authentication.getName()); //redis에서 로그아웃한 유저의 refreshToken정보 삭제

            redisTemplate
                    .opsForValue()
                    .set(accessToken, "logout", tokenExpiration, TimeUnit.MILLISECONDS); //로그아웃한 유저가 logoutDto.accessToken을 재사용하지 못하도록 로그아웃한 유저의 logoutDto.accessToken 정보를 redis에 등록

            return ResponseDto
                    .builder()
                    .httpStatus(HttpStatus.OK.value())
                    .message("로그아웃 되었습니다.")
                    .build();
        }
        catch (CustomBadRequestException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            log.error("logout Unhandled Exception occured", e);
            throw new CustomBadRequestException("로그아웃에 실패하였습니다!");
        }
    }

    /**
     * 기능: 유저 정보 변경
     * 파라미터:
     * userInfo: 변경할 유저 정보
     * 작성자: kor1du
     * 수정자: kor1du
     * 반환 값: 유저 정보 변경 결과
     **/
    public ResponseDto updateInfo(UserDto.Info userInfo)
    {
        ResponseDto returnValue;

        try
        {
            User user = SessionUtil.getUserFromDB();

            //@formatter:off
            if (!user.getNickname().equals(userInfo.getNickname()) && userRepository.findByNickname(userInfo.getNickname()).isPresent()) //별명 중복체크
            // @formatter:on
            {
                throw new CustomBadRequestException("입력하신 별명을 사용하는 유저가 존재합니다. 다른 별명을 사용해주세요!");
            }

            user = user.toBuilder() //유저 정보 변경
                    .userId(user.getUserId())
                    .nickname(userInfo.getNickname())
                    .address(userInfo.getAddress())
                    .addressDetail(userInfo.getAddressDetail())
                    .phone(userInfo.getPhone())
                    .profileImgPath(userInfo.getProfileImgPath())
                    .build();

            userRepository.save(user); //유저 정보 저장

            returnValue = ResponseDto
                    .builder()
                    .httpStatus(HttpStatus.OK.value())
                    .message("정보가 변경되었습니다.")
                    .body(JsonUtil.ObjectToJsonObject("userInfo", modelMapper.map(user, UserDto.Info.class)))
                    .build();
        }
        catch (CustomBadRequestException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            log.error("updateInfo Unhandled Exception occured", e);
            throw new CustomBadRequestException("정보 변경중 오류가 발생하였습니다!");
        }

        return returnValue;
    }
}
