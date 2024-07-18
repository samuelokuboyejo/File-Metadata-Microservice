package com.project.File_Metadata_Microservice;


import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController

public class FIleController {
    @PostMapping("/api/fileupload")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("uploadfile")MultipartFile file){
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path uploadPath = Paths.get("upload");

        try{
            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }
            Files.copy(file.getInputStream(), uploadPath.resolve(fileName));
        } catch (IOException e) {
            throw new RuntimeException("Unable to store your file", e);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("name", fileName);
        response.put("type", file.getContentType());
        response.put("size", file.getSize());
        response.put("uri", ServletUriComponentsBuilder.fromCurrentContextPath().path("/uploads").path(fileName).toUriString());

        return ResponseEntity.ok(response);
    }

}
