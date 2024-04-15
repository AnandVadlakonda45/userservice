package com.scaler.userservice.configs;

import com.scaler.userservice.dtos.BaseResponse;
import com.scaler.userservice.exceptions.UserAlreadyExists;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {
    @ResponseStatus(HttpStatus.ALREADY_REPORTED)
    @ExceptionHandler(UserAlreadyExists.class)
    public BaseResponse handleUserAlreadyExistException(UserAlreadyExists exists){
        return BaseResponse.builder().message(exists.getMessage()).build();
    }
}
