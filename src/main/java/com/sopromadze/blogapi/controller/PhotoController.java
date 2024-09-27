package com.sopromadze.blogapi.controller;

import com.sopromadze.blogapi.model.Photo;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.payload.PhotoRequest;
import com.sopromadze.blogapi.payload.PhotoResponse;
import com.sopromadze.blogapi.security.CurrentUser;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.PhotoService;
import com.sopromadze.blogapi.utils.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/photos")
@RequiredArgsConstructor
public class PhotoController {


	private final PhotoService photoService;

	@GetMapping
	public PagedResponse<PhotoResponse> getAllPhotos(
			@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
		return photoService.getAllPhotos(page, size);
	}

	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public Photo addPhoto(@Valid @RequestBody PhotoRequest photoRequest,
			@CurrentUser UserPrincipal currentUser) {
		Photo photo = photoService.addPhoto(photoRequest, currentUser);

		return photo;
	}

	@GetMapping("/{id}")
	public Photo getPhoto(@PathVariable(name = "id") Long id) {
		Photo photo = photoService.getPhoto(id);

		return photo;
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public Photo updatePhoto(@PathVariable(name = "id") Long id,
							 @Valid @RequestBody PhotoRequest photoRequest, @CurrentUser UserPrincipal currentUser) {

		Photo photo = photoService.updatePhoto(id, photoRequest, currentUser);

		return photo;
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ApiResponse deletePhoto(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal currentUser) {
		ApiResponse apiResponse = photoService.deletePhoto(id, currentUser);

		return apiResponse;
	}
}
