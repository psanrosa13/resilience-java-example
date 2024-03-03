package com.paulasantana.applicationB;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping
public class ControllerB {

    Random random = new Random();

    @GetMapping("/domainB")
    public ResponseEntity<String> consult() throws InterruptedException {

        int numberRandom = random.nextInt(11);
        boolean pair = (numberRandom % 2) == 0;

        if(pair){
           // throw new RuntimeException();
           Thread.sleep(1000L);
        }

        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

}
