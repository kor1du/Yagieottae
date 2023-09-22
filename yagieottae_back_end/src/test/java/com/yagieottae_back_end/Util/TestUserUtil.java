package com.yagieottae_back_end.Util;

import com.yagieottae_back_end.Entity.User;
import com.yagieottae_back_end.Exception.CustomBadRequestException;
import com.yagieottae_back_end.Repository.UserRepository;
import com.yagieottae_back_end.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.stereotype.Component;

@Component
public class TestUserUtil
{
    private static UserRepository userRepository;
    private static UserService userService;

    @Autowired
    public void setUserService(UserService userService)
    {
        TestUserUtil.userService = userService;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository)
    {
        TestUserUtil.userRepository = userRepository;
    }

    public static User getTestUser()
    {
        return userRepository.findByUserId("user").orElseThrow(() -> new CustomBadRequestException("테스트용 유저 없음"));
    }
}
