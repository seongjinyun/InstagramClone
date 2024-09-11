package com.project.instagramclone.entity.posts;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "posts")
public class Posts {
    @Id
    private String id;
    private String userName; // member_id에서 가져오는 작성자 명
    private String content; // 내용
    private String memberId; // 이미지 URL
    private long regdate; // 작성 날짜
}
