package dev.ppondeu.java_starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class JavaStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaStarterApplication.class, args);
    }

}
