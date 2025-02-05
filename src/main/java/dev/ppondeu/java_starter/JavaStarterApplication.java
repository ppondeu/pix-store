package dev.ppondeu.java_starter;

import dev.ppondeu.java_starter.common.interfaces.IFileStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
public class JavaStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaStarterApplication.class, args);
    }

    @Bean
    CommandLineRunner init(IFileStorageService storageService) {
        return (args) -> {
            storageService.init();
        };
    }

}
