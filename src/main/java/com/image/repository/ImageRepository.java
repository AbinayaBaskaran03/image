package com.image.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.image.entity.ImageEntity;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity , UUID>{

}
