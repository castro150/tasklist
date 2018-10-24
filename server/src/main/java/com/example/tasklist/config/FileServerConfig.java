package com.example.tasklist.config;

import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class FileServerConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String filesPath = Paths.get("./files").toAbsolutePath().normalize().toString().replace("\\", "/").concat("/");
        registry
          .addResourceHandler("/files/**")
          .addResourceLocations("file:" + filesPath); 
    }
}