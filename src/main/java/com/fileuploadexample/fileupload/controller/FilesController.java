package com.fileuploadexample.fileupload.controller;

import com.fileuploadexample.fileupload.model.FileInfo;
import com.fileuploadexample.fileupload.model.ResponseMessage;
import com.fileuploadexample.fileupload.services.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@CrossOrigin("http://localhost:5000")
public class FilesController {
    private FileStorageService fileStorageService;

    public FilesController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file")MultipartFile file){
        ResponseMessage response = new ResponseMessage();
        try{
            fileStorageService.save(file);
            response.setMessage(file.getOriginalFilename() + " was uploaded successfully!");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e){
            response.setMessage("Couldn't upload the file " + file.getOriginalFilename() + "!");
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<FileInfo>> getFilesList(){
        List<FileInfo> fileInfos = fileStorageService.loadAll().map(path -> {
            String fileName = path.getFileName().toString();
            String uri = MvcUriComponentsBuilder.fromMethodName(FilesController.class,
                                                    "getFile",
                                                    path.getFileName().toString()).build().toString();
            return new FileInfo(fileName, uri);
        }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename){
        Resource file = fileStorageService.load(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
