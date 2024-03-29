package com.springmvcproject.stickynotes.config;

import com.springmvcproject.stickynotes.model.entity.UserEntity;
import com.springmvcproject.stickynotes.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final static String ROLE_PREFIX="ROLE_";
    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("userName {}",username);
        Optional<UserEntity>user=this.userRepo.findByUsername(username);
        user.orElseThrow(()->new UsernameNotFoundException("user not found "));
        log.info("user{}",user.get());

        String password=user.get().getPassword();
        String role=user.get().getRole();
        log.info("before role {}",role);
        role=ROLE_PREFIX+role;
        log.info("after role_prefix {}",role);
        List<GrantedAuthority>roles=new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(role));
        log.info("role {}",role);
        return new CustomUserDetails(username,password,roles);
    }
}
