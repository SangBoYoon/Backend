package com.sangboyoon.accounter.application.security;

import com.sangboyoon.accounter.domain.user.UserPrincipal;
import com.sangboyoon.accounter.domain.user.UserRepository;
import com.sangboyoon.accounter.domain.user.exceptions.CUserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        return new UserPrincipal(userRepository.findById(Long.valueOf(id)).orElseThrow(CUserNotFoundException::new));
    }
}
