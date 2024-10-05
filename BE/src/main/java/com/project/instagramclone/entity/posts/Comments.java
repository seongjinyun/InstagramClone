package com.project.instagramclone.entity.posts;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "comments")
@Getter
@Setter
@NoArgsConstructor
public class Comments {
    @Id
    private String id;
    private String nickname;
    private String comment;

    private String postId;
    private String parentCommentId; // 대댓글의 경우 부모 댓글의 ID, 없으면 null

    private List<String> children;  // 자식 댓글(대댓글)의 ID 리스트

    private long regdate;
}
