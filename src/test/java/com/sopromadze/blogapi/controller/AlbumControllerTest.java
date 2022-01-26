package com.sopromadze.blogapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.Photo;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.payload.request.AlbumRequest;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.AlbumService;
import com.sopromadze.blogapi.service.PhotoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.sopromadze.blogapi.model.user.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@AutoConfigureMockMvc
class AlbumControllerTest {

    @Mock
    private AlbumService albumService;

    @Mock
    private PhotoService photoService;

    @InjectMocks
    private AlbumController albumController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Bean("customUserDetailsService")
    @Primary
    public UserDetailsService userDetailsService() {
        UserPrincipal admin = new UserPrincipal(125L,"admin", "admin","admin","admin@gmail.com", "563", Collections.singleton(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString())));

        UserPrincipal user = new UserPrincipal(123L,"user", "user","user","user@gmail.com", "123", Collections.singleton(new SimpleGrantedAuthority(RoleName.ROLE_USER.toString())));

        return new InMemoryUserDetailsManager(List.of(admin,user));
    }

    static User usuario;
    static Photo photo1, photo2;
    static List<Photo> photos;
    static Album album;
    static AlbumRequest albumRequest;
    static UserPrincipal user;

    @BeforeEach
    void initTest(){

        user = new UserPrincipal(
                123L,
                "Paco",
                "Lopez",
                "pacols",
                "pacols@gmail.com",
                "123",
                Collections.singleton(new SimpleGrantedAuthority(RoleName.ROLE_USER.toString())));

        usuario = new User();
        usuario.setId(9652L);
        usuario.setUsername("pacols");
        usuario.setEmail("pacosl@gmail.com");


        photo1 = new Photo();
        photo1.setTitle("Foto1");
        photo1.setId(1L);
        photo1.setUrl("www.photo/foto.1.jpg");

        photo2 = new Photo();
        photo1.setTitle("Foto2");
        photo1.setId(1L);
        photo1.setUrl("www.photo/foto.2.jpg");

        photos = new ArrayList<Photo>();
        photos.add(photo1);
        photos.add(photo2);

        album = new Album();
        album.setId(95L);
        album.setTitle("Album 1");
        album.setPhoto(photos);
        album.setUser(usuario);

        albumRequest = new AlbumRequest();
        albumRequest.setId(95L);
        albumRequest.setTitle("Album 1");
        albumRequest.setPhoto(photos);

    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "customUserDetailsService")
    void addAlbum() {

        when(albumService.addAlbum(albumRequest, user)).thenReturn(album);

        Album al = albumController.addAlbum(albumRequest, user);

        assertEquals(album.getId(), al.getId());

    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "customUserDetailsService")
    void addAlbumExceptionUnauthorized() {

        when(albumService.addAlbum(albumRequest, user)).thenReturn(album);

        Album al = albumController.addAlbum(albumRequest, user);

        assertEquals(album.getId(), al.getId());

    }

   /* @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "customUserDetailsService")
    void addAlbumExceptionUnauthorized_thenReturn401() throws Exception{

        mockMvc.perform((RequestBuilder) post("/api/albums")).andExpect(status().isUnauthorized());
    }*/

    @Test
    void getAlbum() {
    }

    @Test
    void updateAlbum() {
    }

    @Test
    void deleteAlbum() {
    }

    @Test
    void getAllPhotosByAlbum() {
    }
}