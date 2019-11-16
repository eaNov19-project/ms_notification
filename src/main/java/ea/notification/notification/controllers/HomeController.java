package ea.notification.notification.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public class HomeController {

    @GetMapping("/")
    public ResponseEntity<?> index(){
        return ResponseEntity.ok().build();
    }
}