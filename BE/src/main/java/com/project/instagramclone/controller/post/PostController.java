package com.project.instagramclone.controller.post;

import com.project.instagramclone.service.post.PostsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name="Post service", description="Post service API")
public class PostController {

    private final PostsService postsService;

    @PostMapping("/create/post")
    public String CreatePost(){



        return "Ok";
    }
}
