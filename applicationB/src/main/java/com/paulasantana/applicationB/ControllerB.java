package com.paulasantana.applicationB;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class ControllerB {

    @GetMapping("/domainB")
    public ResponseEntity<String> consult(){
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

}
