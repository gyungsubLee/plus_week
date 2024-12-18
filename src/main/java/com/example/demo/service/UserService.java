package com.example.demo.service;

import com.example.demo.dto.Authentication;
import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.UserRequestDto;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public Long signupWithEmail(UserRequestDto userRequestDto) {
        String encodedPassword = PasswordEncoder.encode(userRequestDto.getPassword());
        userRequestDto.updatePassword(encodedPassword);

        User saveUser = userRepository.save(userRequestDto.toEntity());

        return saveUser.getId();
    }

    @Transactional(readOnly = true)
    public Authentication loginUser(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail());
        if (user == null || !PasswordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 사용자 이름 혹은 잘못된 비밀번호");
        }
        return new Authentication(user.getId(), user.getRole());
    }
}
