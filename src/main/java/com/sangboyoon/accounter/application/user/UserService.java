package com.sangboyoon.accounter.application.user;

import com.sangboyoon.accounter.configuration.security.JwtTokenProvider;
import com.sangboyoon.accounter.domain.bookmark.BookmarkRepository;
import com.sangboyoon.accounter.domain.corporation.LikeRepository;
import com.sangboyoon.accounter.domain.security.exceptions.CAccessDeniedException;
import com.sangboyoon.accounter.domain.user.User;
import com.sangboyoon.accounter.domain.user.UserRepository;
import com.sangboyoon.accounter.domain.user.exceptions.CUserNotFoundException;
import com.sangboyoon.accounter.web.users.dto.UpdateUserRequest;
import com.sangboyoon.accounter.web.users.dto.UserDto;
import com.sangboyoon.accounter.web.users.sign.dto.DeleteUserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BookmarkRepository bookmarkRepository;
    private final LikeRepository likeRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;

    @Transactional(readOnly = true)
    public UserDto findUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(CUserNotFoundException::new);
        return new UserDto(user);
    }

    @Transactional
    public UserDto update(Long userId, UpdateUserRequest updateUserRequest) {
        User modifiedUser = userRepository.findById(userId).orElseThrow(CUserNotFoundException::new);
        modifiedUser.update(passwordEncoder.encode(updateUserRequest.getPassword()), updateUserRequest.getNickName());
        return new UserDto(modifiedUser);
    }

    @Transactional
    public String deleteUser(DeleteUserRequest request) {

        String accessToken = request.getAccessToken();
        if (!jwtTokenProvider.validationToken(accessToken)) throw new CAccessDeniedException();
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(CUserNotFoundException::new);

        bookmarkRepository.deleteAllByUserId(user.getId());
        likeRepository.deleteAllByUserId(user.getId());
        userRepository.deleteById(user.getId());

        return "delete Success";
    }
}
