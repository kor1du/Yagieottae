package com.yagieottae_back_end.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

@Configuration
public class MapperConfig
{
    ModelMapper modelMapper = new ModelMapper();

    @Bean
    ModelMapper modelMapper()
    {
        return modelMapper;
    }
}
