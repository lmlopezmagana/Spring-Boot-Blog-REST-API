package com.sopromadze.blogapi.repository;

import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.security.UserPrincipal;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    void findByCreatedBy() {

        UserPrincipal userP = new UserPrincipal(3L,"Manuel", "Fernández", "ManuFer", "manufer@gmail.com", "123456789", Collections.singleton(new SimpleGrantedAuthority(RoleName.ROLE_USER.toString())));

        Post post1 = new Post();
        Post post2 = new Post();
        Post post3 = new Post();

        Pageable pageable = PageRequest.of(1, 1, Sort.Direction.DESC, "createdAt");
        Iterable<Post> posts = postRepository.findByCreatedBy(3L, pageable);
        assertThat(posts).hasSize(3).contains(post1, post2, post3);

    }

    @Test
    void findByCategory() {
    }

    @Test
    void findByTagsIn() {
    }

    @Test
    void countByCreatedBy() {
    }
}