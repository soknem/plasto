package com.plasto.api.mapper;

import com.plasto.api.domain.User;
import com.plasto.api.feature.user.dto.UserRequest;
import com.plasto.api.feature.user.dto.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User fromRequest(UserRequest userRequest);

    UserResponse toUserResponse(User user);
}
