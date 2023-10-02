package com.yagieottae_back_end;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class YagieottaeBackEndApplicationTests
{
    public static void main(String[] args)
    {
        SpringApplication.run(YagieottaeBackEndApplication.class, args);
    }

//    @Test
//    void contextLoads()
//    {
//    }

}
