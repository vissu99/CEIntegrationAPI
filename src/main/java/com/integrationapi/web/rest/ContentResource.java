package com.integrationapi.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.integrationapi.config.ApplicationProperties;
import com.integrationapi.domain.UploadFileResponse;
import com.integrationapi.service.ContentService;
import com.integrationapi.service.FileStorageService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Controller for view and managing Log Level at runtime.
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
public class ContentResource {

    @Autowired
    private ContentService contentService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    ApplicationProperties applicationProperties;

    @GetMapping("/contents")
    public String getElements() {
        return applicationProperties.getEndpoint() + applicationProperties.getUser() + applicationProperties.getOrg() + applicationProperties.getGoogle();
    }

    @ApiOperation(value = "Get Contents in folder", notes = "Contents", tags = { "contents" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "get run status", response = String.class) })
    @GetMapping(path = "/getContents/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getFolderContents(@ApiParam(value = "fromPath", required = false) @RequestParam(name = "fromPath", required = false, value = "") String fromPath) {
        String response = contentService.getContents(fromPath);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @ApiOperation(value = "Upload a file to a folder", notes = "Contents", tags = { "folder" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "uploaded successfully", response = UploadFileResponse.class) })
    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFileV2(@RequestParam("file") MultipartFile file, @RequestParam("toPath") String toPath) {
        String fileName = fileStorageService.uploadFile(toPath, file);

        return new UploadFileResponse(fileName, "",
                file.getContentType(), file.getSize());
    }

    @ApiOperation(value = "Download a file from a folder", notes = "Contents", tags = { "folder" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "download link provided", response = String.class) })
    @GetMapping(path = "/download", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> downloadLink(@RequestParam String fromPath) {
        String response = contentService.getDownloadLink(fromPath);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
