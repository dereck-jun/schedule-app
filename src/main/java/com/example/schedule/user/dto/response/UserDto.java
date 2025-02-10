package com.example.schedule.user.dto.response;

import com.example.schedule.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UserDto {

    private final Long userId;
    private final String username;
    private final LocalDateTime createdDateTime;
    private final LocalDateTime updatedDateTime;

    public static UserDto from(User user) {
        return new UserDto(
            user.getId(),
            user.getUsername(),
            user.getCreatedDateTime(),
            user.getUpdatedDateTime()
        );
    }
}
