package com.sopromadze.blogapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = {SpringSecurityTestWebConfig.class})
@AutoConfigureMockMvc
class CategoryControllerTest {

    @MockBean
    CategoryServiceImpl categoryService;

    @InjectMocks
    CategoryController categoryController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    static Long CATEGORY_ID=1L;
    static Category category;
    static UserPrincipal user;


    @BeforeEach
    void init(){
        user = new UserPrincipal(1L,"user", "user","user","user@gmail.com", "user", Collections.singleton(new SimpleGrantedAuthority(RoleName.ROLE_USER.toString())));
        category=new Category(); category.setId(CATEGORY_ID); category.setName("Viaje");
    }

    @Test
    @WithUserDetails("user")
    void addCategory_Success() throws Exception {
        when(categoryService.addCategory(category,user)).thenReturn(category);

        mockMvc.perform(
            post("/api/categories")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isCreated()).andReturn();
    }
    @Test
    @WithUserDetails("admin")
    void addCategory_Forbidden() throws Exception {
        mockMvc.perform(
            post("/api/categories")
                .contentType("application/json")
                .content(objectMapper.writeValueAsBytes(category)))
            .andExpect(status().isForbidden()).andReturn();
    }
    @Test
    void addCategory_UnAuthorized() throws Exception {
        mockMvc.perform(
            post("/api/categories")
                .contentType("application/json")
                .content(objectMapper.writeValueAsBytes(category)))
            .andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    void getCategory_Success() throws Exception{
        when(categoryService.getCategory(anyLong())).thenReturn(category);
        mockMvc.perform(get("/api/categories/{id}",CATEGORY_ID)
                .contentType("application/json"))
                .andExpect(jsonPath("$.id").value(CATEGORY_ID))
                .andExpect(status().isOk());
    }

}