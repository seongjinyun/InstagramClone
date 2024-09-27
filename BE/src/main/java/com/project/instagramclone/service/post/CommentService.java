package com.project.instagramclone.service.post;

import com.project.instagramclone.dto.post.CommentDTO;
import com.project.instagramclone.entity.posts.Comments;
import com.project.instagramclone.repository.post.CommentRepository;
import com.project.instagramclone.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    public static CommentRepository commentRepository;
    public static PostRepository postRepository;

    public Comments postComment(CommentDTO commentDTO){

        return null;
    }
}
