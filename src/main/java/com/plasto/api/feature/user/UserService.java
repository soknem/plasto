package com.plasto.api.feature.user;

import com.plasto.api.feature.user.dto.UserRequest;
import com.plasto.api.feature.user.dto.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse createUser(UserRequest userRequest);

    UserResponse getUserByUuid(String uuid);

    List<UserResponse> getAllUsers();

    void deleteUser(String uuid);
}
