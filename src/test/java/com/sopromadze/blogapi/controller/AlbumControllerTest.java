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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.sopromadze.blogapi.model.user.User;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestWebConfig.class})
@AutoConfigureMockMvc
class AlbumControllerTest {

    @MockBean
    private AlbumService albumService;

    @MockBean
    private PhotoService photoService;

    @InjectMocks
    private AlbumController albumController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "customUserDetailsServiceImpl")
    void addAlbum() {

        when(albumService.addAlbum(albumRequest, user)).thenReturn(album);

        Album al = albumController.addAlbum(albumRequest, user);

        assertEquals(album.getId(), al.getId());

    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "customUserDetailsServiceImpl")
    void addAlbumExceptionUnauthorized() {

        when(albumService.addAlbum(albumRequest, user)).thenReturn(album);

        Album al = albumController.addAlbum(albumRequest, user);

        assertEquals(album.getId(), al.getId());

    }

    @Test
    void addAlbumExceptionUnauthorized_thenReturn401() throws Exception{

        mockMvc.perform(post("/api/albums"))
                .andExpect(status().isUnauthorized());
    }

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