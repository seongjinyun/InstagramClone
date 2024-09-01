package com.project.instagramclone.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Tag(name="Admin service", description="Admin service API")
public class AdminController {

    @PostMapping("/admin")
    public String adminP() {
        return "Admin Page";
    }

}
