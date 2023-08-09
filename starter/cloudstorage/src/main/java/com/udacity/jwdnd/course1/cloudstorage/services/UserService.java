package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
@AllArgsConstructor

public class UserService {

    private final UserMapper userMapper;
    private final HashService hashService;

    public boolean isUsernameAvailable(String username) {
        return userMapper.getUser(username) == null;
    }

    public int createUser(User user) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);

        // Create a new User object and set its properties
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setSalt(encodedSalt);
        newUser.setPassword(hashedPassword);
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());

        return userMapper.insert(newUser);
    }

}
