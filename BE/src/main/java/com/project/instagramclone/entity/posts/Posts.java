package com.project.instagramclone.entity.posts;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "posts")
@Getter
@Setter
@NoArgsConstructor
public class Posts {
    @Id
    private String id;
    private String nickname; // member_id에서 가져오는 작성자 명
    private String content; // 내용

    //username으로 조
//    @ManyToOne
//    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
//    private String memberId; // 회원 순번
    private long regdate; // 작성 날짜
}
