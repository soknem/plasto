package com.plasto.api.feature.user;

import com.plasto.api.domain.User;
import com.plasto.api.domain.UserStatus;
import com.plasto.api.feature.user.dto.UserRequest;
import com.plasto.api.feature.user.dto.UserResponse;
import com.plasto.api.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse createUser(UserRequest userRequest) {

        User user = userMapper.fromRequest(userRequest);
        user.setUuid(UUID.randomUUID().toString());

        userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse getUserByUuid(String uuid) {
        User user =
                userRepository.findByUuid(uuid).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("user = %s has not been found", uuid)));

        return userMapper.toUserResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @Override
    public void deleteUser(String uuid) {
        User user =
                userRepository.findByUuid(uuid).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("user = %s has not been found", uuid)));
        userRepository.delete(user);
    }
}
