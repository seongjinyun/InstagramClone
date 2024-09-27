package com.project.instagramclone.repository.post;

import com.project.instagramclone.entity.posts.Comments;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comments, String> {
}
