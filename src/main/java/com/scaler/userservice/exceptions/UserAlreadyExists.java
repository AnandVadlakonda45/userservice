package com.scaler.userservice.exceptions;

public class UserAlreadyExists extends Exception{
    private String message;
    public UserAlreadyExists(String message){
        super(message);
    }
}
