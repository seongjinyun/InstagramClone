package com.project.instagramclone.dto.post;

import lombok.*;

@Getter
@Setter
@Builder

@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {

    private String postId;
    private String comment;
    private String parentComment;
    private String nickname; // 댓글 작성자 - nickname

}
