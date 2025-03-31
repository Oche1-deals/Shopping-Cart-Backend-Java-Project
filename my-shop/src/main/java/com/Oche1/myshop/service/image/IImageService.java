package com.Oche1.myshop.service.image;

import com.Oche1.myshop.dto.ImageDto;
import com.Oche1.myshop.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImages(Long productId, List<MultipartFile> files);
    void updateImage(MultipartFile file, Long imageId);
}
