package com.example.schedule.user.service;

import com.example.schedule.global.common.exception.ErrorDetail;
import com.example.schedule.user.dto.request.UserDeleteRequest;
import com.example.schedule.user.dto.request.UserLoginRequest;
import com.example.schedule.user.dto.request.UserRegisterRequest;
import com.example.schedule.user.dto.request.UserUpdateRequest;
import com.example.schedule.user.dto.response.UserDto;
import com.example.schedule.user.entity.User;
import com.example.schedule.user.exception.*;
import com.example.schedule.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.schedule.global.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /* 회원 가입 */
    @Transactional
    public UserDto register(UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail()) && userRepository.existsByUsername(request.getUsername())) {
            throw new EmailDuplicatedException(List.of(
                new ErrorDetail(DUPLICATED, "email", "중복된 이메일입니다."),
                new ErrorDetail(DUPLICATED, "username", "중복된 사용자명입니다.")
            ));
        }
        checkEmailDuplicate(request.getEmail());
        checkUsernameDuplicate(request.getUsername());
        User newUser = saveUser(request);
        return UserDto.from(newUser);
    }

    /* 로그인 */
    @Transactional(readOnly = true)
    public UserDto login(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new UserLoginFailedException(List.of(
                new ErrorDetail(LOGIN_FAILED, null, "로그인에 실패하였습니다.")
            )));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserLoginFailedException(List.of(
                new ErrorDetail(LOGIN_FAILED, null, "로그인에 실패하였습니다.")
            ));
        }
        return UserDto.from(user);
    }

    /* 유저 프로필 수정 */
    @Transactional
    public UserDto updateUserProfile(Long userId, UserUpdateRequest request, UserDto sessionUserDto) {
        checkUserPermission(userId, sessionUserDto);
        if (request.getUsername() == null && request.getChangePassword() == null) {
            throw new UserUpdateException(List.of(
                new ErrorDetail(INVALID_INPUT, "username", "모든 필드가 비어있을 수 없습니다."),
                new ErrorDetail(INVALID_INPUT, "changePassword", "모든 필드가 비어있을 수 없습니다.")
            ));
        }
        User findUser = findUserByIdOrThrow(userId);
        passwordValidation(request.getPassword(), findUser.getPassword());

        if (request.getUsername() != null) {
            checkPreviousUsername(request.getUsername(), findUser);
            checkUsernameDuplicate(request.getUsername());
            findUser.setUsername(request.getUsername());
        }

        if (request.getChangePassword() != null) {
            checkPreviousPassword(request.getChangePassword(), findUser);
            findUser.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        return UserDto.from(findUser);
    }

    /* 유저 단건 조회 */
    @Transactional(readOnly = true)
    public UserDto findUser(Long userId) {
        User findUser = findUserByIdOrThrow(userId);
        return UserDto.from(findUser);
    }

    /* 유저 전체 조회 */
    @Transactional(readOnly = true)
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
            .map(UserDto::from)
            .toList();
    }

    /* 유저 삭제 */
    @Transactional
    public void deleteUser(Long userId, UserDeleteRequest request, UserDto sessionUserDto) {
        checkUserPermission(userId, sessionUserDto);
        User findUser = findUserByIdOrThrow(userId);
        passwordValidation(request.getPassword(), findUser.getPassword());
        userRepository.delete(findUser);
    }

    private void checkUserPermission(Long userId, UserDto sessionUserDto) {
        if (!userId.equals(sessionUserDto.getUserId())) {
            throw new UserAccessDeniedException(List.of(
                new ErrorDetail(FORBIDDEN, null, "해당 기능을 수행하기 위한 권한이 없습니다.")
            ));
        }
    }

    /* 타 도메인에서 Entity 자체를 필요로 하기 때문에 생성 */
    public User findUserByIdOrThrow(Long userId) {
        return userRepository.findUserById(userId)
            .orElseThrow(() -> new UserNotFoundException(List.of(
                new ErrorDetail(NOT_FOUND, null, "요청에 해당하는 유저를 찾을 수 없습니다.")
            )));
    }

    private User saveUser(UserRegisterRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        return userRepository.save(
            User.of(request.getUsername(), request.getEmail(), encodedPassword));
    }

    private void passwordValidation(String requestPassword, String savedPassword) {
        if (!passwordEncoder.matches(requestPassword, savedPassword)) {
            throw new PasswordMismatchException(List.of(
                new ErrorDetail(MISMATCH, "password", "패스워드가 일치하지 않습니다.")
            ));
        }
    }

    private void checkPreviousPassword(String password, User findUser) {
        if (passwordEncoder.matches(findUser.getPassword(), password)) {
            throw new UserUpdateException(List.of(
                new ErrorDetail(INVALID_INPUT, "changePassword", "이전 비밀번호와 동일할 수 없습니다.")
            ));
        }
    }

    private void checkUsernameDuplicate(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new UsernameDuplicatedException(List.of(
                new ErrorDetail(DUPLICATED, "username", "중복된 사용자명입니다.")
            ));
        }
    }

    private void checkEmailDuplicate(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailDuplicatedException(List.of(
                new ErrorDetail(DUPLICATED, "email", "중복된 이메일입니다.")
            ));
        }
    }

    private void checkPreviousUsername(String username, User findUser) {
        if (findUser.getUsername().equals(username)) {
            throw new UserUpdateException(List.of(
                new ErrorDetail(INVALID_INPUT, "username", "이전 사용자명과 동일할 수 없습니다.")
            ));
        }
    }
}
