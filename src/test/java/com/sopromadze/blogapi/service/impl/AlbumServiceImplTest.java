package com.sopromadze.blogapi.service.impl;

import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.AlbumResponse;
import com.sopromadze.blogapi.payload.request.AlbumRequest;
import com.sopromadze.blogapi.repository.AlbumRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AlbumServiceImplTest {

    @Mock
    AlbumRepository albumRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    AlbumServiceImpl albumService;

    static Long ALBUM_ID=1L;
    static Long USER_ID=1L;
    static Album album;
    static User user;
    static AlbumRequest albumRequest;
    static AlbumResponse albumResponse;
    static UserPrincipal userPrincipal;
    static Role adminRole;

    @BeforeEach
    void init(){
        albumResponse=new AlbumResponse();
        user=new User(); user.setId(USER_ID);
        adminRole=new Role(RoleName.ROLE_ADMIN);
        album=new Album();
        albumRequest=new AlbumRequest(); albumRequest.setTitle("Cantando bajo la lluvia");
        userPrincipal= new UserPrincipal(USER_ID, "Jose","Ramirez", "admin","joselito@gmail.com","admin", Collections.singleton(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString())));
    }

    @Test
    void getAlbum_Success (){
        when(albumRepository.findById(anyLong())).thenReturn(Optional.of(album));
        assertNotNull(albumService.getAlbum(ALBUM_ID));
    }

    @Test
    void getAlbum_Exception(){
        when(albumRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,()-> albumService.getAlbum(ALBUM_ID));
    }

    @Test
    void addAlbum_Success(){
        album.setUser(user);
        doNothing().when(modelMapper).map(albumRequest,album);
        when(userRepository.getUser(any(UserPrincipal.class))).thenReturn(user);
        when(albumRepository.save(any(Album.class))).thenReturn(album);
        assertNotNull(albumService.addAlbum(albumRequest,userPrincipal).getUser());
    }

    @Test
    void updateAlbum_Success(){
        String title= "Cantando bajo la lluvia";
        Album a2 =new Album(); a2.setUser(user); a2.setTitle(title);

        when(albumRepository.findById(anyLong())).thenReturn(Optional.of(album));
        when(userRepository.getUser(any(UserPrincipal.class))).thenReturn(user);
        album.setUser(user);

        when(albumRepository.save(a2)).thenReturn(a2);

        doNothing().when(modelMapper).map(any(Album.class),a2);

        assertEquals(title, albumService.updateAlbum(ALBUM_ID,albumRequest,userPrincipal).getTitle());

    }






}