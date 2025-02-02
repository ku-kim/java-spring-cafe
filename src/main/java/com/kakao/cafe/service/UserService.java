package com.kakao.cafe.service;

import com.kakao.cafe.domain.User;
import com.kakao.cafe.domain.UserJoinRequest;
import com.kakao.cafe.domain.UserLoginRequest;
import com.kakao.cafe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 회원 가입
     * @param userJoinRequest 가입할 유저 정보
     * @return 회원 가입된 유저의 id
     */
    public String join(UserJoinRequest userJoinRequest) {
        User user = userJoinRequest.toDomain();
        checkDuplicateUser(user);
        userRepository.save(user);
        return user.getUserId();
    }

    private void checkDuplicateUser(User user) {
        userRepository.findByUserId(user.getUserId())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("이미 존재하는 회원입니다.");
                });
    }

    /**
     * 회원 전체 조회
     * @return 유저 리스트
     */
    public List<User> findUsers() {
        return userRepository.findAll();
    }

    /**
     * userId 회원 조회
     * @param userId 회원 조회할 유저 아이디
     * @return 해당 유저
     */
    public User findUser(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
    }

    public User login(UserLoginRequest userLoginRequest) {
        return userRepository.findByUserId(userLoginRequest.getUserId())
                .filter(user -> user.getPassword().equals(userLoginRequest.getPassword()))
                .orElse(null);
    }
}
