package com.project.instagramclone.repository.post;

import com.project.instagramclone.entity.posts.Posts;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Posts, String> {
    //authorIds에는 팔로우한 유저의 목록이 들어가야 함 - AuthorId는 지금 사용중인 username으로 수정해야함
    List<Posts> findPostsByUserNameIn(List<String> username);
}
