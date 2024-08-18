package com.project.instagramclone.controller;

import com.project.instagramclone.dto.form.JoinDto;
import com.project.instagramclone.service.form.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @PostMapping("/join")
    public String joinProc(@ModelAttribute JoinDto joinDto) {
        joinService.join(joinDto);
        return "ok";
    }

}
