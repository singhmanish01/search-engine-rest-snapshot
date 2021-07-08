package com.mss.searchengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.nio.file.FileSystems;


@EnableScheduling
@SpringBootApplication
public class RestApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestApplication.class, args);

        System.out.println(FileSystems.getDefault().getPath("DOCS").toAbsolutePath()+"\\");
    }

}

