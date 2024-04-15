package com.scaler.userservice.services;

import com.scaler.userservice.exceptions.UserAlreadyExists;
import com.scaler.userservice.models.Token;
import com.scaler.userservice.models.User;
import com.scaler.userservice.repositories.TokenRepository;
import com.scaler.userservice.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, TokenRepository tokenRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User signup(String fullname,String email, String password)throws UserAlreadyExists{
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()){
            throw new UserAlreadyExists("User already Exits with this email : " + email);
        }

        User u = new User();
        u.setEmail(email);
        u.setName(fullname);
        u.setHashedPassword(bCryptPasswordEncoder.encode(password));
        User user1 = userRepository.save(u);

        return user1;
    }

    public Token login(String email, String password){
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()){
            return null;
        }
        User user = optionalUser.get();

        if(!bCryptPasswordEncoder.matches(password, user.getHashedPassword())){
            return null;
        }

        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysLater = today.plusDays(30);

        Date expiryDate = Date.from(thirtyDaysLater.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Token token = new Token();

        token.setUser(user);
        token.setExpiryAt(expiryDate);
        //doubt
        token.setValue(RandomStringUtils.randomAlphanumeric(128));

        Token savedToken = tokenRepository.save(token);

        return savedToken;
    }

    public void logOut(String token){
        Optional<Token> optionalToken = tokenRepository.findByValueAndDeletedEquals(token,false);

        if (token.isEmpty()){
            return;
        }
        Token token1 = optionalToken.get();
        token1.setDeleted(true);
        tokenRepository.save(token1);

        return;
    }
    public User validateToken(String token){
        Optional<Token> token1 = tokenRepository.findByValueAndDeletedEqualsAndExpiryAtGreaterThan(token, false, new Date());
        if (token1.isEmpty()){
            return null;
        }
        return token1.get().getUser();
    }

}
