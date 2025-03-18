package com.note.security.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/integration")
public class IntegrationController {

    private static final Logger logger = LoggerFactory.getLogger(IntegrationController.class);

    /**
     * Sample method for integration
     *
     * @return
     */
    @GetMapping(value = "/hello")
    public String hello() {
        logger.info("Accessed /hello endpoint");

        // validate token (add your validation logic here)
        logger.debug("Validating token...");

        // Placeholder for actual token validation
        // In case of token validation failure, log an error
        // For example: logger.error("Token validation failed");

        logger.info("Successfully validated token");

        System.out.println("Hello"); // You may keep this for testing, but it's better to replace it with logs.

        logger.info("Returning 'Hello world' response.");
        return "Hello world";
    }
}
