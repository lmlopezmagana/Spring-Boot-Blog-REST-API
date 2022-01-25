package com.sopromadze.blogapi.servicios;

import com.sopromadze.blogapi.model.*;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.repository.CategoryRepository;
import com.sopromadze.blogapi.repository.PostRepository;
import com.sopromadze.blogapi.repository.TagRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.service.impl.PostServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PostServiceTest {

    @Mock
    PostRepository postRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    TagRepository tagRepository;

    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    PostServiceImpl postService;

    @Test
    void getPostByTag_TagIdGiven_ShouldShowPagedPostListWithGivenTag() {

        User user = new User("Ernesto", "Fatuarte", "efatuarte", "ernesto.fatuarte@gmail.com", "efatuarte");
        Comment com1 = new Comment("Me ha encantado tu post. Enhorabuena!");
        Category cat1 = new Category("Programación");
        Tag tag1 = new Tag("Flutter");
        Tag tag2 = new Tag("Spring");

        when(tagRepository.findByName("Spring")).thenReturn(tag1);
        when(tagRepository.findByName("Flutter")).thenReturn(tag2);

        Post post1 = Post.builder()
                .id(1L)
                .title("Hola Mundo")
                .body("Mi primer post")
                .user(user)
                .category(cat1)
                .comments(List.of(com1))
                .tags(List.of(tag1))
                .build();
        Post post2 = Post.builder()
                .id(2L)
                .title("Aprendiendo a hacer testing con Mockito!")
                .body("Mi primer test del proyecto")
                .user(user)
                .category(cat1)
                .comments(List.of(com1))
                .tags(List.of(tag2))
                .build();

        when(postRepository.save(post1)).thenReturn(post1);
        when(postRepository.save(post2)).thenReturn(post2);

        Page<Post> pageResult = new PageImpl<>(List.of(post2));

        PagedResponse<Post> result = new PagedResponse<>();
        result.setContent(pageResult.getContent());
        result.setTotalPages(1);
        result.setTotalElements(1);
        result.setLast(true);
        result.setSize(1);

        when(postRepository.findByTagsIn(Collections.singletonList(tag2), any(Pageable.class))).thenReturn(pageResult);

        assertEquals(result, postService.getPostsByTag(2L, 0, 10));
    }


    @Test
    void assertNotNullRepo() {
        assertNotNull(tagRepository);
    }


}
