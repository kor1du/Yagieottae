package com.yagieottae_back_end.Jwt;

import com.yagieottae_back_end.Entity.User;
import com.yagieottae_back_end.Exception.CustomBadRequestException;
import com.yagieottae_back_end.Repository.UserRepository;
import com.yagieottae_back_end.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtCustomUserDetailsService implements UserDetailsService
{
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userID) throws CustomBadRequestException
    {
        User user = userRepository.findByUserId(userID).orElseThrow(() -> new CustomBadRequestException("해당 유저가 존재하지 않습니다!"));
        
        JwtCustomUserDetails userDetails = createUserDetails(user);

        return userDetails;
    }

    private JwtCustomUserDetails createUserDetails(User user)
    {
        return JwtCustomUserDetails.builder()
                                   .id(user.getUserId())
                                   .password(user.getPassword())
                                   .authority(user.getRole().toString())
                                   .build();
    }
}
