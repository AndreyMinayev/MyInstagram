package com.example.Instagram.web;

import com.example.Instagram.entity.Image;
import com.example.Instagram.payload.response.MessageResponse;
import com.example.Instagram.services.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("api/image")
@CrossOrigin
public class ImageController {

    @Autowired
    private ImageUploadService imageUploadService;

    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> uploadImageToUser(@RequestParam ("file") MultipartFile file,
                                                             Principal principal) throws IOException {
        imageUploadService.uploadImageToUser(file, principal);
        return  ResponseEntity.ok(new MessageResponse("Image Uploaded Succesfully"));

    }

    @PostMapping("/{postId}/upload")
    public ResponseEntity<MessageResponse> uploadImageToPost(@PathVariable ("postId") String postId,
                                                             @RequestParam ("file")MultipartFile file,
                                                             Principal principal)throws IOException {

        imageUploadService.uploadImageToPost(file,principal, Long.parseLong(postId));
        return  ResponseEntity.ok(new MessageResponse("Image Uploaded Succesfully"));
    }

    @GetMapping("/profileImage")
    public ResponseEntity<Image> getImageForUser(Principal principal){
        Image userImage = imageUploadService.getImageToUser(principal);

        return new ResponseEntity<>(userImage, HttpStatus.OK);
    }

    @GetMapping("/{postId}/image")
    public ResponseEntity<Image> getImageForPost(@PathVariable String postId, Principal principal){
        Image postImage = imageUploadService.getImageToPost(Long.parseLong(postId));

        return new ResponseEntity<>(postImage, HttpStatus.OK);
    }


}
