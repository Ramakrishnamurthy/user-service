package com.note.security.controller;


import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/integration")
public class IntegrationController {

    /**
     *sample method for integration
     * @return
     */
    @GetMapping(value = "/hello")
    public String hello() {
        // validate token
        System.out.println("Hello");
        return "Hello world";
    }

}

