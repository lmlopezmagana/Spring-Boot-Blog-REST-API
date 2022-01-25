package com.sopromadze.blogapi.service;

import com.sopromadze.blogapi.model.Photo;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.payload.PhotoRequest;
import com.sopromadze.blogapi.payload.PhotoResponse;
import com.sopromadze.blogapi.security.UserPrincipal;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PhotoService {

	PagedResponse<PhotoResponse> getAllPhotos(int page, int size);

	Photo getPhoto(Long id);

	Photo updatePhoto(Long id, PhotoRequest photoRequest, UserPrincipal currentUser);

	Photo addPhoto(PhotoRequest photoRequest, UserPrincipal currentUser);

	ApiResponse deletePhoto(Long id, UserPrincipal currentUser);

	PagedResponse<PhotoResponse> getAllPhotosByAlbum(Long albumId, int page, int size);

}