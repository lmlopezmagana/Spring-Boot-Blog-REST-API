package com.sopromadze.blogapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.Comment;
import com.sopromadze.blogapi.model.Tag;
import com.sopromadze.blogapi.payload.CommentRequest;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.CommentService;
import javassist.NotFoundException;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Log
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestWebConfig.class})
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService service;


    @Test
    void getAllComments() throws Exception {

        Comment c = new Comment();
        c.setName("comentario");
        c.setBody("bodybodybodybody");
        c.setEmail("email");
        c.setId(1L);

        PagedResponse<Comment> comentarios = new PagedResponse();
        comentarios.setContent(List.of(c));

        when(service.getAllComments(any(Long.class), any(Integer.class), any(Integer.class))).thenReturn(comentarios);

        MvcResult result = mockMvc.perform(get("/api/posts/{postId}/comments", 1L))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andReturn();

        log.info(result.getResponse().getContentAsString());
    }

/*
    @Test
    @WithUserDetails("user")
    void addTag_Success () throws Exception {
        when(tagService.addTag(any(Tag.class),any(UserPrincipal.class))).thenReturn(tag);

        mockMvc.perform(post("/api/tags")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(tag)))
                .andExpect(status().isCreated());
    }
*/
    @Test
    @WithUserDetails("user")
    void addComment() throws Exception{

        Comment c = new Comment();
        c.setName("comentario");
        c.setBody("bodybodybodybody");
        c.setEmail("email");
        c.setId(1L);

        PagedResponse<Comment> comentarios = new PagedResponse();
        comentarios.setContent(List.of(c));

        when(service.addComment(any(CommentRequest.class), any(Long.class), any(UserPrincipal.class))).thenReturn(c);

        mockMvc.perform(post("/api/posts/{postId}/comments", 1L)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(c)))
                .andExpect(status().isOk());




    }

    @Test
    void getComment() {
    }

    @Test
    void updateComment() {
    }

    @Test
    void deleteComment() {
    }
}