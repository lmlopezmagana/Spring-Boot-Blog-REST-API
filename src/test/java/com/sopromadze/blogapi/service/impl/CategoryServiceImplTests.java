package com.sopromadze.blogapi.service.impl;

import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.repository.CategoryRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.validation.constraints.AssertTrue;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CategoryServiceImplTests {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryServiceImpl;


    @Test
    void getCategorySuccessTest() {

        //Mockeamos lo necesario.
        Category category = new Category(
                "paisaje"
        );
        category.setId(1L);
        Optional<Category> c = Optional.of(category);

        Mockito.when(categoryRepository.findById(1l)).thenReturn(c);

        //Implementamos el test

        Assertions.assertEquals(category, categoryServiceImpl.getCategory(1L));

    }

    @Test
    void getCategoryExceptionTest() {

        //Mockeamos lo necesario
        Optional<Category> category = Optional.empty();

        Mockito.when(categoryRepository.findById(1L)).thenReturn(category);

        //Implementamos el test
        Assertions.assertThrows(ResourceNotFoundException.class, () -> categoryServiceImpl.getCategory(1L));

    }

    @Test
    void addCategorySuccessTest() {

        //Mockeamos lo necesario
        Category category = new Category(
                "paisaje"
        );
        category.setId(1L);

        UserPrincipal currentUser = new UserPrincipal(
                1L,
                "Ale",
                "Bajo", "diale",
                "luismimaquina@gmail.com",
                "1234",
                null
        );

        Mockito.when(categoryServiceImpl.addCategory(category, currentUser)).thenReturn(category);

        //Implementamos el test
        Assertions.assertEquals(category, category);

    }


}
