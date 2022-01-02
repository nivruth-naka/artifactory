package com.miwtech.artifactory.service;

import com.miwtech.artifactory.entity.UserEntity;
import com.miwtech.artifactory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final UserEntity userEntity = userRepository.findByUsername(username);
        if (Objects.isNull(userEntity)) {
            throw new UsernameNotFoundException(username);
        }
        return User.withUsername(userEntity.getUsername())
                .password("{noop}" + userEntity.getPassword())
                .authorities("USER")
                .build();
    }

}
