package com.scaler.userservice.controllers;

import com.scaler.userservice.dtos.LoginRequestDto;
import com.scaler.userservice.dtos.LogoutRequestDto;
import com.scaler.userservice.dtos.SignUpRequestDto;
import com.scaler.userservice.dtos.UserDto;
import com.scaler.userservice.exceptions.UserAlreadyExists;
import com.scaler.userservice.models.Token;
import com.scaler.userservice.models.User;
import com.scaler.userservice.services.UserService;
import jakarta.annotation.Nonnull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Token login(@RequestBody LoginRequestDto requestDto){
        return userService.login(requestDto.getEmail(), requestDto.getPassword());
    }
    @PostMapping("/signup")
    public UserDto signUp(@RequestBody SignUpRequestDto requestDto) throws UserAlreadyExists {
        String name = requestDto.getName();
        String password = requestDto.getPassword();
        String email = requestDto.getEmail();

        return UserDto.from(userService.signup(name,email,password));
    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto requestDto){
        userService.logOut(requestDto.getToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/validate/{token}")
    public UserDto validateToken(@PathVariable("token") @Nonnull String token){
        return UserDto.from(userService.validateToken(token));
    }
}
