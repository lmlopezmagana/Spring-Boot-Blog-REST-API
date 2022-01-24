package com.sopromadze.blogapi.servicios;

import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.model.Comment;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.Tag;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.repository.CategoryRepository;
import com.sopromadze.blogapi.repository.PostRepository;
import com.sopromadze.blogapi.repository.TagRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.service.impl.PostServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
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
    public void getPostByTag_PostListGiven_ShouldShowPostListWithGivenTag() {

        User user = new User("Ernesto", "Fatuarte", "efatuarte", "ernesto.fatuarte@gmail.com", "efatuarte");
        Comment com1 = new Comment("Me ha encantado tu post. Enhorabuena!");
        Category cat1 = new Category("Programación");
        Tag tag1 = new Tag("Spring");
        Tag tag2 = new Tag("Flutter");

        lenient().when(tagRepository.findByName("Spring")).thenReturn(tag1);
        lenient().when(tagRepository.findByName("Flutter")).thenReturn(tag2);

        Post newPost = new Post(1L, "Aprendiendo a hacer testing con Mockito!", "Mi primer test del proyecto", user, cat1, List.of(com1), List.of(tag1));
    }

}
