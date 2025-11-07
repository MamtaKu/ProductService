package com.demo.productService.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // This annotation indicates that this class is a REST controller
@RequestMapping("/sample")
public class SampleController {

    @GetMapping("/hello/{name}/{age}")
    public String sayHello(@PathVariable("name") String name,
                           @PathVariable("age") int age){
        return "Hello " + name + ": This is your age " + age ;
    }

    @GetMapping("/goodbye")
    public String sayGoodbye(){
        return "Goodbye from SampleController";
    }
}
