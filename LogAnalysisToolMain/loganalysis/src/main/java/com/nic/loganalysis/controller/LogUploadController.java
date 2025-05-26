package com.nic.loganalysis.controller;

import com.nic.loganalysis.dto.UploadResponseDTO;
import com.nic.loganalysis.service.logProcessingService.LogProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/logs")
public class LogUploadController {

    @Autowired
    private LogProcessingService logProcessingService;

    @PostMapping("/upload")
    public ResponseEntity<UploadResponseDTO> uploadLogFile(@RequestParam("file") MultipartFile file) {
        try {
            UploadResponseDTO response = logProcessingService.processLogFile(file);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new UploadResponseDTO("Error processing file: " + e.getMessage(), false));
        }
    }
}