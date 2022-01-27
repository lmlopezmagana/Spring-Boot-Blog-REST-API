package com.sopromadze.blogapi.controller;

import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.security.UserPrincipal;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Collections;
import java.util.List;

@TestConfiguration
public class SpringSecurityTestWebConfig {

    @Bean("customUserDetailsServiceImpl")
    @Primary
    public UserDetailsService userDetailsService() {
        UserPrincipal admin = new UserPrincipal(125L,"admin", "admin","admin","admin@gmail.com", "563", Collections.singleton(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString())));

        UserPrincipal user = new UserPrincipal(123L,"user", "user","user","user@gmail.com", "123", Collections.singleton(new SimpleGrantedAuthority(RoleName.ROLE_USER.toString())));

        return new InMemoryUserDetailsManager(List.of(admin,user));
    }
}
