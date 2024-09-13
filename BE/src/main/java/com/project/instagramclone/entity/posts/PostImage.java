package com.project.instagramclone.entity.posts;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "postImage")
@Getter
@Setter
@NoArgsConstructor
public class PostImage {
    @Id
    private String id;
    @Indexed
    private String postId; // posts 컬렉션의 ID를 참조 (비식별 관계)
    private String mediaUrl; // 파일 URL

}