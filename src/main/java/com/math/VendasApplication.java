package com.math;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@SpringBootApplication
public class VendasApplication {



    public static void main(String[] args) {
        SpringApplication.run(VendasApplication.class,args);
    }


}
