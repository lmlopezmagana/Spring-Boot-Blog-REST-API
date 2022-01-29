package com.sopromadze.blogapi.service.impl;

import com.sopromadze.blogapi.exception.*;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.Address;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.*;
import com.sopromadze.blogapi.repository.PostRepository;
import com.sopromadze.blogapi.repository.RoleRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userService;

    private static UserPrincipal userPrincipal;
    private static User user;
    private static Role role;
    private static Role role2;
    private static InfoRequest infoRequest;

    @BeforeEach
    void setUp() {
        userPrincipal = new UserPrincipal(12L, "Pepe", "Palomo", "pepepalomo", "pepepalomo@gmail.com", "1234", Collections.singleton(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString())));

        Address address = new Address();
        address.setId(2L);
        address.setCity("Sevilla");
        address.setStreet("Calle x");

        user = new User();
        user.setUsername("pepepalomo");
        user.setId(12L);
        user.setCreatedAt(Instant.now());
        user.setFirstName("Pepe");
        user.setLastName("Palomo");
        user.setAddress(address);
        user.setPhone("654548798");
        user.setWebsite("www.palomo.com");

        role = new Role();
        role.setName(RoleName.ROLE_USER);
        role.setId(1L);

        role2 = new Role();
        role.setName(RoleName.ROLE_ADMIN);
        role.setId(2L);

        infoRequest = new InfoRequest();
        infoRequest.setCity("Sevilla");
        infoRequest.setCompanyName("Empresa");
        infoRequest.setZipcode("554");
    }

    @Test
    void getCurrentUser() {

        UserSummary userSummary = userService.getCurrentUser(userPrincipal);

        assertEquals(userSummary.getId(), userPrincipal.getId());
    }

    @Test
    void checkUsernameAvailability() {
        when(userRepository.existsByUsername(userPrincipal.getUsername())).thenReturn(true);

        UserIdentityAvailability userIdentityAvailability = userService.checkUsernameAvailability(userPrincipal.getUsername());

        assertNotNull(userIdentityAvailability);
    }

    @Test
    void checkEmailAvailability() {

        when(userRepository.existsByEmail(userPrincipal.getEmail())).thenReturn(true);

        UserIdentityAvailability userIdentityAvailability = userService.checkUsernameAvailability(userPrincipal.getEmail());

        assertNotNull(userIdentityAvailability);
    }

    @Test
    void getUserProfile() {
        when(userRepository.getUserByName(user.getUsername())).thenReturn(user);

        when(postRepository.countByCreatedBy(user.getId())).thenReturn(1L);

        UserProfile userProfile = userService.getUserProfile(user.getUsername());

        assertNotNull(userProfile);
    }

    @Test
    void addUser() {

        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);

        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.of(role));

        when(userRepository.save(user)).thenReturn(user);

        assertNotNull(userService.addUser(user));
    }

    @Test
    void addUserExceptionUserName(){
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);

        assertThrows(BadRequestException.class, ()-> userService.addUser(user));
    }

    @Test
    void addUserExceptionEmail(){
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        assertThrows(BadRequestException.class, ()-> userService.addUser(user));
    }

    @Test
    void addUserExceptionRole(){
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);

        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.empty());

        assertThrows(AppException.class, ()-> userService.addUser(user));
    }

    @Test
    void updateUser() {
        when(userRepository.getUserByName(user.getUsername())).thenReturn(user);

        when(userRepository.save(user)).thenReturn(user);

        User usuarioUpdate = userService.updateUser(user, user.getUsername(), userPrincipal);

        assertEquals(usuarioUpdate.getId(), userPrincipal.getId());
    }

    /*@Test
    void updateUserExceptionUser(){
        when(userRepository.getUserByName(user.getUsername())).thenReturn(user);

        user.setId(555L);

        assertThrows(UnauthorizedException.class, ()-> userService.updateUser(user, user.getUsername(), userPrincipal));
    }*/

    @Test
    void deleteUser() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        ApiResponse apiResponse = userService.deleteUser(user.getUsername(), userPrincipal);

        verify(userRepository).deleteById(user.getId());

        assertTrue(apiResponse.getSuccess());

    }

    @Test
    void deleteUserExceptionUser(){
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, ()-> userService.deleteUser(user.getUsername(), userPrincipal));
    }

    @Test
    void deleteUserExceptionAccesDenied(){
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        user.setId(548L);

        assertThrows(AccessDeniedException.class, ()-> userService.deleteUser(user.getUsername(), userPrincipal));
    }

    /*@Test
    void giveAdmin() {

        when(userRepository.getUserByName(user.getUsername())).thenReturn(user);

        when(roleRepository.findByName(role.getName())).thenReturn(Optional.of(role2));

        when(roleRepository.findByName(role.getName())).thenReturn(Optional.of(role));

        when(userRepository.save(user)).thenReturn(user);

        ApiResponse apiResponse = userService.giveAdmin(user.getUsername());

        assertTrue(apiResponse.getSuccess());
    }*/

    @Test
    void giveAdminExceptionAdminRole(){
        when(userRepository.getUserByName(user.getUsername())).thenReturn(user);

        when(roleRepository.findByName(role.getName())).thenReturn(Optional.empty());

        assertThrows(AppException.class, () -> userService.giveAdmin(user.getUsername()));
    }

    @Test
    void giveAdminExceptionUserRole(){
        when(userRepository.getUserByName(user.getUsername())).thenReturn(user);

        when(roleRepository.findByName(role.getName())).thenReturn(Optional.of(role2));

        when(roleRepository.findByName(role.getName())).thenReturn(Optional.empty());

        assertThrows(AppException.class, () -> userService.giveAdmin(user.getUsername()));
    }

   /* @Test
    void removeAdmin() {
        when(userRepository.getUserByName(user.getUsername())).thenReturn(user);

        when(roleRepository.findByName(role.getName())).thenReturn(Optional.of(role));

        when(userRepository.save(user)).thenReturn(user);

        ApiResponse apiResponse = userService.removeAdmin(user.getUsername());

        assertTrue(apiResponse.getSuccess());

    }*/

    @Test
    void removeAdminExceptionUserRole(){
        when(userRepository.getUserByName(user.getUsername())).thenReturn(user);

        when(roleRepository.findByName(role.getName())).thenReturn(Optional.of(role2));

        assertThrows(AppException.class, ()-> userService.removeAdmin(user.getUsername()));

    }

    @Test
    void setOrUpdateInfo() {

        when(userRepository.findByUsername(userPrincipal.getUsername())).thenReturn(Optional.of(user));

        when(userRepository.save(user)).thenReturn(user);

        when(postRepository.countByCreatedBy(user.getId())).thenReturn(1L);

        UserProfile userProfile = userService.setOrUpdateInfo(userPrincipal, infoRequest);

        assertEquals(userProfile.getId(), user.getId());

    }

    @Test
    void setOrUpdateInfoExceptionUser(){
        when(userRepository.findByUsername(userPrincipal.getUsername())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, ()-> userService.setOrUpdateInfo(userPrincipal, infoRequest));

    }

    /*@Test
    void setOrUpdateInfoAccessDenied(){
        when(userRepository.findByUsername(userPrincipal.getUsername())).thenReturn(Optional.of(user));

        user.setId(549L);

        assertThrows(AccessDeniedException.class, ()-> userService.setOrUpdateInfo(userPrincipal, infoRequest));

    }*/
}