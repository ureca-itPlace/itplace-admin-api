package com.itplace.adminapi.security.auth.service;

import com.itplace.adminapi.security.auth.dto.CustomUserDetails;
import com.itplace.adminapi.user.entity.User;
import com.itplace.adminapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("아이디 / 비밀번호를 다시 확인해주세요."));
        return new CustomUserDetails(user);
    }
}
