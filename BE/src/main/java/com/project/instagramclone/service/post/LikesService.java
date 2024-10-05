package com.project.instagramclone.service.post;

import com.project.instagramclone.entity.posts.Likes;
import com.project.instagramclone.repository.post.LikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikesService {
    private final LikesRepository likesRepository;

    //좋아요
    public void addLike(String postId, String nickname) {
        if (!likesRepository.existsByPostIdAndNickname(postId, nickname)) {
            Likes like = new Likes();
            like.setPostId(postId);
            like.setNickname(nickname);
            likesRepository.save(like);
        }
    }

    //좋아요 취소
    public void removeLike(String postId, String userId) {
        likesRepository.deleteByPostIdAndNickname(postId, userId);
    }
}

