package com.plasto.api.feature.user.dto;

import com.plasto.api.domain.UserStatus;

public record UserResponse(

        String uuid,

        String name,

        String description,

        UserStatus userStatus,

        String avatar
) {
}
