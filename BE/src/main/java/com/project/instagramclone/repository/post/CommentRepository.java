package com.project.instagramclone.repository.post;

import com.project.instagramclone.entity.posts.Comments;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comments, String> {
    // postId로 해당 게시글의 댓글 목록을 조회
    List<Comments> findByPostId(String postId);

    // 부모 댓글 ID로 해당 부모 댓글의 자식 댓글(대댓글) 목록 조회
    List<Comments> findByParentCommentId(String parentCommentId);
}
