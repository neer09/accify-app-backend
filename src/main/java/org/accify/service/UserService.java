package org.accify.service;

import org.accify.dto.UserRequest;
import org.accify.dto.LoginRequest;
import org.accify.entity.User;
import org.accify.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User register(UserRequest dto) {
        User user = new User();
        user.setUserName(dto.getUserName());
        user.setPassword(dto.getPassword());
        user.setBusinessAccountName(dto.getBusinessAccountName());
        return userRepository.save(user);
    }

    public boolean validate(LoginRequest login) {
        return userRepository.findByUserNameAndPassword(
                login.getUserName(),
                login.getPassword()
        ).isPresent();
    }
}
