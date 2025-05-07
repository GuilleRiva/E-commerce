package com.ecommerce.ecommerce.security;

import com.ecommerce.ecommerce.model.Users;
import com.ecommerce.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Attempting to load user by Username: {}",
                username);

        Users user = userRepository.findByUsername(username)
                .orElseThrow(()-> {
                    log.warn("User not found:{}", username);
                    return  new UsernameNotFoundException("User not found with username: " + username);
                });

        return new UserDetailsImpl(user);
    }
}
