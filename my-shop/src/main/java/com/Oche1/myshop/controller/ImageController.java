package com.Oche1.myshop.controller;

import com.Oche1.myshop.dto.ImageDto;
import com.Oche1.myshop.exceptions.ResourceNotFoundEception;
import com.Oche1.myshop.model.Image;
import com.Oche1.myshop.response.ApiResponse;
import com.Oche1.myshop.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {
    private final IImageService imageService;
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files, @RequestParam Long productId){
        try {
            List<ImageDto> imageDtos = imageService.saveImages(files,productId);
            return ResponseEntity.ok(new ApiResponse("upload succesful", imageDtos));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("upload failed!",e.getMessage()));
        }
    }

    @GetMapping("/images/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {
        Image image = imageService.getImageById(imageId);
        ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1,(int)image.getImage().length()));
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachement; filename=\"" +image.getFileName() +"\"")
                .body(resource);
    }

@PutMapping("/image/{imageId}/update")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId, @RequestBody MultipartFile file){
        try {
            Image image= imageService.getImageById(imageId);
            if(image != null){
                imageService.updateImage(file,imageId);
                return ResponseEntity.ok(new ApiResponse("update success!",null));
            }
        } catch (ResourceNotFoundEception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("update failed!", INTERNAL_SERVER_ERROR));
    }

    @DeleteMapping("/image/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId){
        try {
            Image image= imageService.getImageById(imageId);
            if(image != null){
                imageService.deleteImageById(imageId);
                return ResponseEntity.ok(new ApiResponse("delete success!",null));
            }
        } catch (ResourceNotFoundEception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("delete failed!", INTERNAL_SERVER_ERROR));
    }
}
