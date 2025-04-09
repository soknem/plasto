package com.plasto.api.feature.user.dto;

import com.plasto.api.domain.UserStatus;
import jakarta.persistence.Column;

public record UserRequest(

        String name,

        String description,

        UserStatus userStatus,

        String avatar
) {
}
