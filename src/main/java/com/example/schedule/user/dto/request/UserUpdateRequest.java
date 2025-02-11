package com.example.schedule.user.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserUpdateRequest {

    @Nullable
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Size(min = 2, max = 12, message = "길이는 2 ~ 12 사이여야 합니다.")
    private final String username;

    @NotBlank
    private final String password;

    @Nullable
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private final String changePassword;

    public UserUpdateRequest(@Nullable String username, String password, @Nullable String changePassword) {
        this.username = username;
        this.password = password;
        this.changePassword = changePassword;
    }
}
