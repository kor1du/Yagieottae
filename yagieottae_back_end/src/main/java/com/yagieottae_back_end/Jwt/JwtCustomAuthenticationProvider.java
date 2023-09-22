package com.yagieottae_back_end.Jwt;

import com.yagieottae_back_end.Entity.User;
import com.yagieottae_back_end.Exception.CustomBadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtCustomAuthenticationProvider implements AuthenticationProvider
{
    private final JwtCustomUserDetailsService jwtCustomUserDetailsService;

    private final PasswordEncoder passwordEncoder;

    /**
     * 기능: 사용자가 입력한 비밀번호와 DB에 저장되어 있는 비밀번호를 비교한다
     * 파라미터:
     * authentication: 사용자가 입력한 유저 정보가 들어있는 authentication 객체
     * 작성자: kor1du
     * 수정자: kor1du
     * 반환 값: 인증받은 유저 정보
     **/
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        String id = authentication
                .getPrincipal()
                .toString();
        String password = authentication
                .getCredentials()
                .toString();
        
        UserDetails customUserDetails = jwtCustomUserDetailsService.loadUserByUsername(id); //DB에 저장되어 있는 유저 정보
        String encodedPassword = customUserDetails.getPassword(); //DB에 암호화되어 저장되어 있는 유저 비밀번호

        if (!passwordEncoder.matches(password, encodedPassword)) //유저가 입력한 비밀번호가 DB에 저장되어 있는 비밀번호와 다르면
        {
            throw new CustomBadRequestException("비밀번호가 일치하지 않습니다. 입력하신 내용을 다시 확인해주세요.");
        }

        return new UsernamePasswordAuthenticationToken(id, password, customUserDetails.getAuthorities());
    }


    @Override
    public boolean supports(Class<?> authentication)
    {
        return true;
    }
}
