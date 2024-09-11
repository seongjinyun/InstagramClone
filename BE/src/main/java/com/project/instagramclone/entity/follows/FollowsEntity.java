package com.project.instagramclone.entity.follows;

import com.project.instagramclone.entity.member.MemberEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "follows")
@Setter
@Getter
@NoArgsConstructor
public class FollowsEntity {

    @Id
    @Column(name = "followsId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long followsId;

    // 팔로우를 한 사람
    @ManyToOne
    @JoinColumn(name = "follower_id", referencedColumnName = "memberId")
    private MemberEntity follower;

    // 팔로우를 당한 사람
    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "memberId")
    private MemberEntity member;

}
