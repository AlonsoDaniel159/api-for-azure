package com.alonso.firstapi.azure.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ProfileController {

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @GetMapping("/api/profile")
    public Map<String, String> profile() {
        return Map.of("activeProfile", activeProfile);
    }
}