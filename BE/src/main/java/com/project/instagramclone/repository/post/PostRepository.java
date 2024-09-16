package com.project.instagramclone.repository.post;

import com.project.instagramclone.entity.posts.Posts;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Posts, String> {

}
