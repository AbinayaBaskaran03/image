package com.image.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.image.entity.ImageEntity;
import com.image.service.ImageService;

@RestController
@RequestMapping(value = "/image")
public class ImageController {

	@Autowired
	public ImageService imageService;

	@PostMapping("/upload")
	public ResponseEntity<String> uploadImage(@RequestParam("name") String name,
			@RequestParam("file") MultipartFile file, @RequestParam("status") String status,
			@RequestParam("date") String date) {
		try {
			ImageEntity imageEntity = new ImageEntity();
			imageEntity.setName(name);
			imageEntity.setImage(file.getBytes());
			imageEntity.setStatus(status);
			LocalDate dateobj = LocalDate.now();
			imageEntity.setDate(dateobj);
			imageService.saveImage(imageEntity);
			return ResponseEntity.ok("Image uploaded successfully");
		} catch (IOException e) {
			return ResponseEntity.badRequest().body("Image upload failed" + e);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<byte[]> downloadImage(@PathVariable UUID id) {
		ImageEntity imageObj = imageService.getImageById(id);
		if (imageObj != null) {
			MediaType mediaType = MediaType.IMAGE_JPEG;

			String fileExtension = getFileExtension(imageObj.getName());

			if (fileExtension != null) {
				mediaType = getMediaType(fileExtension);
			}

			String contentDisposition = "attachment; filename=" + imageObj.getName();

			return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
					.contentType(mediaType).body(imageObj.getImage());
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	private String getFileExtension(String fileName) {
		int dotIndex = fileName.lastIndexOf('.');
		return (dotIndex == -1) ? null : fileName.substring(dotIndex + 1);
	}

	private MediaType getMediaType(String fileExtension) {
		switch (fileExtension.toLowerCase()) {
		case "jpg":
		case "jpeg":
		case "png":
		case "pdf":
		case "mp4":
			return MediaType.IMAGE_JPEG;
		default:
			return MediaType.APPLICATION_OCTET_STREAM;
		}
	}
}
