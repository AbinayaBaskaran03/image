package com.image.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.image.entity.ImageEntity;
import com.image.repository.ImageRepository;

@Service
public class ImageService {
	
	@Autowired
	public ImageRepository imageRepository;

	public void saveImage(ImageEntity imageEntity) {
		imageRepository.save(imageEntity);	
	}

	public ImageEntity getImageById(UUID id) {
		return imageRepository.findById(id).orElse(null);
	}
	

	
	

}
