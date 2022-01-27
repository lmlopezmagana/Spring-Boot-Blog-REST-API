package com.sopromadze.blogapi.controller;

import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.CustomUserDetailsService;
import com.sopromadze.blogapi.service.impl.CustomUserDetailsServiceImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Collections;
import java.util.List;

@TestConfiguration
public class SpringSecurityTestWebConfig {

    /*@Bean("customUserDetailsServiceImpl")
    @Primary
    public UserDetailsService userDetailsService() {
        UserPrincipal admin = new UserPrincipal(125L,"admin", "admin","admin","admin@gmail.com", "563", Collections.singleton(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString())));

        UserPrincipal user = new UserPrincipal(123L,"user", "user","user","user@gmail.com", "123", Collections.singleton(new SimpleGrantedAuthority(RoleName.ROLE_USER.toString())));

        return new InMemoryUserDetailsManager(List.of(admin,user));
    }*/

    @Bean("customUserDetailsServiceImpl")
    @Primary
    public CustomUserDetailsService customUserDetailsService() {
        return new CustomUserDetailsService() {

            UserPrincipal admin = new UserPrincipal(125L,"admin", "admin","admin","admin@gmail.com", "563", Collections.singleton(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString())));

            UserPrincipal user = new UserPrincipal(123L,"user", "user","user","user@gmail.com", "123", Collections.singleton(new SimpleGrantedAuthority(RoleName.ROLE_USER.toString())));

            UserDetailsService uds = new InMemoryUserDetailsManager(List.of(admin,user));


            @Override
            public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
                return uds.loadUserByUsername(usernameOrEmail);
            }

            @Override
            public UserDetails loadUserById(Long id) {
                switch (id.intValue()) {
                    case 125: return admin;
                    case 123: return user;
                    default: throw new UsernameNotFoundException("Usuario no encontrado");
                }
            }
        };
    }
}
