package com.sopromadze.blogapi.service;

import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.Tag;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.repository.TagRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.impl.TagServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    TagServiceImpl tagService;

    @Test
    void getTagById_success(){

        Tag t = new Tag("yoquese");

        when(tagRepository.findById(anyLong())).thenReturn(Optional.of(t));

        assertEquals(t, tagService.getTag(4L));
    }

    @Test
    void getTagById_failure(){

        when(tagRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tagService.getTag(4L));
    }

    @Test
    void addTag_success(){

        Tag t = new Tag("yoquese");

        Role r = new Role(RoleName.ROLE_USER);

        List<Role> roles = new ArrayList<Role>();
        roles.add(r);

        User user = new User();
        user.setUsername("user");
        user.setRoles(roles);
        user.setId(1L);

        UserPrincipal userP = UserPrincipal.create(user);

        when(tagRepository.save(t)).thenReturn(t);

        assertEquals(t, tagService.addTag(t, userP));

    }

    @Test
    void addTag_fail(){

        Tag t = new Tag(null);

        Role r = new Role(RoleName.ROLE_USER);

        List<Role> roles = new ArrayList<Role>();
        roles.add(r);

        User user = new User();
        user.setUsername("user");
        user.setRoles(roles);
        user.setId(1L);

        UserPrincipal userP = UserPrincipal.create(user);

        when(tagRepository.save(t)).thenReturn(null);

        assertEquals(null, tagService.addTag(t, userP));

    }


}