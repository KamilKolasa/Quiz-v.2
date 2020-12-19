package com.app.service;

import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.User;
import com.app.model.dto.UserDto;
import com.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto addUser(UserDto user) {
        if (user == null) {
            throw new MyException(ExceptionCode.SERVICE, "ADD USER - USER IS NULL");
        }
        User u = ModelMapper.fromUserDtoToUser(user);
        return ModelMapper.fromUserToUserDto(userRepository.save(u));
    }

    public UserDto updateUser(UserDto user) {
        if (user == null) {
            throw new MyException(ExceptionCode.SERVICE, "UPDATE USER - USER IS NULL");
        }
        User u = ModelMapper.fromUserDtoToUser(user);
        return ModelMapper.fromUserToUserDto(userRepository.save(u));
    }

    public UserDto deleteUser(Long id) {
        if (id == null) {
            throw new MyException(ExceptionCode.SERVICE, "DELETE USER - ID IS NULL");
        }
        User user = userRepository.findById(id).orElseThrow(() -> new NullPointerException("NOT FOUND USER"));;
        userRepository.delete(user);
        return ModelMapper.fromUserToUserDto(user);
    }

    public Optional<UserDto> findOneUser(Long id) {
        if (id == null) {
            throw new MyException(ExceptionCode.SERVICE, "FIND ONE USER - ID IS NULL");
        }
        return userRepository.findById(id).map(p -> ModelMapper.fromUserToUserDto(p));
    }

    public List<UserDto> findAllUser() {
        return userRepository
                .findAll()
                .stream()
                .map(p -> ModelMapper.fromUserToUserDto(p))
                .collect(Collectors.toList());
    }
}
