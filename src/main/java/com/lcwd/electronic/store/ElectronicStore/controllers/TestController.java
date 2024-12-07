package com.lcwd.electronic.store.ElectronicStore.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping
    public String testing() {
        return "Welcome to electronic Store";
    }
}
