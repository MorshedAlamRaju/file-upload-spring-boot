package com.fileuploadexample.fileupload;

import com.fileuploadexample.fileupload.services.FileStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class FileuploadApplication implements CommandLineRunner {

    @Resource
    private FileStorageService fileStorageService;

    public static void main(String[] args) {
        SpringApplication.run(FileuploadApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        fileStorageService.deleteAll();
        fileStorageService.init();
    }
}
