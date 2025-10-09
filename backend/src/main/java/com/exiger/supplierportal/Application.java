package com.exiger.supplierportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.HashMap;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@SpringBootApplication
public class Application {

    // @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    // @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    // ResponseEntity<Map<String, String>> home() {
    //     return ResponseEntity.ok(Map.of("message", "Hello World!"));
    // }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    ResponseEntity<Map<String, String>> echoCookies(HttpServletRequest request) {
        Map<String, String> out = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                out.put(c.getName(), c.getValue());
            }
        }
        return ResponseEntity.ok(out);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}