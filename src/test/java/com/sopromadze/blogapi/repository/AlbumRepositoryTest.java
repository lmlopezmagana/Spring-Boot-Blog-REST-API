package com.sopromadze.blogapi.repository;

import com.sopromadze.blogapi.model.Album;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AlbumRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AlbumRepository albumRepository;
    private static Album album;

    @BeforeAll
    static void beforeAll() {
        album = new Album();
    }

    @Test
    void findByCreatedBy() {

        //Persistimos una instancia
        entityManager.persist(album);

        //Implementamos el test
        //Assertions.assertNotNull(albumRepository.findByCreatedBy(1L));

    }
}