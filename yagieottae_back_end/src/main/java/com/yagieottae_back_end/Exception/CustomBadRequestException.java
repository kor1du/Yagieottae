package com.yagieottae_back_end.Exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CustomBadRequestException extends RuntimeException
{
    public CustomBadRequestException(String message)
    {
        super(message);
    }
}
