package com.sopromadze.blogapi.service.impl;

import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.Comment;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.payload.CommentRequest;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.repository.CommentRepository;
import com.sopromadze.blogapi.repository.PostRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    private static final Long POST_ID = 1234L;
    private static final Long COMMENT_ID = 456L;
    private static final Long USER_ID = 352L;
    private static final int PAGE = 1;
    private static final int SIZE = 1;
    private static final String BODY = "Hola que hace";
    private static final String ID_STR = "id";
    private static final String POST_STR = "Post";

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void getAllComments() {
        Pageable pageable = getPageable();
        when(commentRepository.findByPostId(POST_ID,pageable)).thenReturn(getComments());

        PagedResponse<Comment> response = commentService.getAllComments(POST_ID, PAGE, SIZE);
        assertEquals(1,response.getTotalElements());
    }

    private Page<Comment> getComments() {

        return new PageImpl<Comment>(Collections.singletonList(getCommentEntity()));
    }

    private User getUser(){
        User user = new User();
        user.setUsername("Pepe");
        user.setLastName("Palotes");
        user.setId(USER_ID);

        return user;
    }

    private Pageable getPageable() {
        return PageRequest.of(PAGE,SIZE, Sort.Direction.DESC, "createdAt");

    }

    @Test
    void addComment() {
        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(getPost()));

        when(userRepository.getUser(getUserPrincipal())).thenReturn(getUser());

        Comment commentSave = getCommentEntity();

        commentSave.setId(null);

        when(commentRepository.save(commentSave)).thenReturn(getCommentEntity());

        Comment comment = commentService.addComment(getCommentRequest(), POST_ID, getUserPrincipal());

    }

    @Test
    void getComment() {
        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(getPost()));

        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(getCommentEntity()));

        Comment comment = commentService.getComment(POST_ID, COMMENT_ID);

        assertEquals(POST_ID, comment.getPost().getId());
    }

   /* @Test
    void getCommentPostException(){
        when(postRepository.findById(POST_ID)).thenThrow(ResourceNotFoundException.class);

        commentService.getComment(POST_ID,COMMENT_ID);
    }*/

   /* @Test
    void getCommentCommentException(){
        when(commentRepository.findById(COMMENT_ID)).thenThrow(ResourceNotFoundException.class);

        commentService.getComment(POST_ID,COMMENT_ID);
    }*/


    @Test
    void updateComment() {
        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(getPost()));

        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(getCommentEntity()));

        when(commentRepository.save(getCommentEntity())).thenReturn(getCommentEntity());

        Comment comment = commentService.updateComment(POST_ID, COMMENT_ID, getCommentRequest(), getUserPrincipal());

        assertEquals(COMMENT_ID, comment.getId());
    }

   /* @Test()
    void updateCommentExceptionPost(){

        when(postRepository.findById(POST_ID)).thenThrow(new ResourceNotFoundException(POST_STR, ID_STR, POST_ID));

        commentService.updateComment(POST_ID, COMMENT_ID, getCommentRequest(), getUserPrincipal());

        ResourceNotFoundException thrown = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            new ResourceNotFoundException(POST_STR, ID_STR, POST_ID);
        });

        Assertions.assertEquals(POST_ID,thrown.getFieldValue());
    }*/

    private UserPrincipal getUserPrincipal() {
        UserPrincipal userPrincipal = new UserPrincipal(USER_ID, "Pepe","Palote", "pepalote","pepalote@gmail.com","1234", Collections.singleton(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString())));
        return userPrincipal;
    }

    private Comment getCommentEntity() {
        Comment comment = new Comment();
        comment.setName("pepalote");
        comment.setEmail("pepalote@gmail.com");
        comment.setUser(getUser());
        comment.setPost(getPost());
        comment.setId(COMMENT_ID);
        comment.setBody(BODY);
        return comment;
    }

    private CommentRequest getCommentRequest(){
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setBody(BODY);
        return commentRequest;
    }

    private Post getPost() {
        Post post = new Post();
        post.setUser(getUser());
        post.setId(POST_ID);

        return post;
    }

    @Test
    void deleteComment() {

        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(getPost()));

        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(getCommentEntity()));

        ApiResponse apiResponse = commentService.deleteComment(POST_ID, COMMENT_ID, getUserPrincipal());

        verify(commentRepository).deleteById(COMMENT_ID);

        assertTrue(apiResponse.getSuccess());
    }
}