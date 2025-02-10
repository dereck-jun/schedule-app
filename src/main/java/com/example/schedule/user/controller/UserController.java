package com.example.schedule.user.controller;

import com.example.schedule.global.common.response.ApiResponse;
import com.example.schedule.user.dto.request.UserDeleteRequest;
import com.example.schedule.user.dto.request.UserLoginRequest;
import com.example.schedule.user.dto.request.UserRegisterRequest;
import com.example.schedule.user.dto.request.UserUpdateRequest;
import com.example.schedule.user.dto.response.UserDto;
import com.example.schedule.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.schedule.global.common.SessionConst.LOGIN_MEMBER;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/api/v1/users")
    public ApiResponse<UserDto> register(@Valid @RequestBody UserRegisterRequest request) {
        UserDto register = userService.register(request);
        return ApiResponse.success(OK, register, "회원가입 성공");
    }

    @PostMapping(value = "/api/v1/users/login")
    public ApiResponse<UserDto> login(@Valid @RequestBody UserLoginRequest loginRequest,
                                      HttpServletRequest request) {
        UserDto loginUserDto = userService.login(loginRequest);

        HttpSession session = request.getSession();
        session.setAttribute(LOGIN_MEMBER, loginUserDto);

        return ApiResponse.success(OK, loginUserDto, "로그인 성공");
    }

    @GetMapping(value = "/api/v1/users/{userId}")
    public ApiResponse<UserDto> findUser(@PathVariable Long userId) {
        UserDto findUser = userService.findUser(userId);
        return ApiResponse.success(OK, findUser, "계정 단건 조회 성공");
    }

    @GetMapping(value = "/api/v1/users")
    public ApiResponse<List<UserDto>> findAllUsers() {
        List<UserDto> findUserDtos = userService.findAllUsers();
        return ApiResponse.success(OK, findUserDtos, "계정 전체 조회 성공");
    }

    @PatchMapping(value = "/api/v1/users/{userId}")
    public ApiResponse<UserDto> updateUserProfile(@PathVariable Long userId,
                                                  @Valid @RequestBody UserUpdateRequest updateRequest) {
        UserDto updatedUserDto = userService.updateUserProfile(userId, updateRequest);
        return ApiResponse.success(OK, updatedUserDto, "계정 프로필 수정 성공");
    }

    @PostMapping(value = "/api/v1/users/{userId}/delete")
    public ApiResponse<Void> deleteUser(@PathVariable Long userId,
                                        @Valid @RequestBody UserDeleteRequest deleteRequest) {
        userService.deleteUser(userId, deleteRequest);
        return ApiResponse.success(OK, null, "계정 삭제 성공");
    }
}
