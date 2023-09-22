package com.yagieottae_back_end.Util;

import com.yagieottae_back_end.Entity.User;
import com.yagieottae_back_end.Exception.CustomBadRequestException;
import com.yagieottae_back_end.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SessionUtil
{
    private static UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository)
    {
        SessionUtil.userRepository = userRepository;
    }

    //세션에 있는 유저 정보를 토대로 DB에 저장되어 있는 유저 정보반환
    public static User getUserFromDB()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userId = authentication.getName();

        return userRepository.findByUserId(userId).orElseThrow(() -> new CustomBadRequestException("유저정보가 존재하지 않습니다!"));
    }
}
