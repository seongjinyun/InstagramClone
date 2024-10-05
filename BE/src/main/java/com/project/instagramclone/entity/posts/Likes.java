package com.project.instagramclone.entity.posts;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "likes")
@Getter
@Setter
@NoArgsConstructor
public class Likes {
    @Id
    private String id;
    private String postId; // 게시글 ID
    private String nickname; // 사용자 닉네임
}
