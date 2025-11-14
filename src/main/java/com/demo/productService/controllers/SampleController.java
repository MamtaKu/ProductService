package com.demo.productService.controllers;

import org.springframework.web.bind.annotation.*;

@RestController // This annotation indicates that this class is a REST controller
@RequestMapping("/sample")
public class SampleController {

    @GetMapping("/hello")
    public String sayHello(@RequestParam("name") String name,
                           @RequestParam("age") int age){
        return "Hello " + name + ": This is your age " + age ;
    }

    @GetMapping("/goodbye")
    public String sayGoodbye(){
        return "Goodbye from SampleController";
    }
}
