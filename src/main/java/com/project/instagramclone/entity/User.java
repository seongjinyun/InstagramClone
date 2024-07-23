package com.project.instagramclone.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity                       // DB 테이블과 매핑할 대상임을 명시
                              // Entity 이름을 지정하지 않으면 기본값인 클래스 이름 사용
@Table(name = "`user`")       // user 테이블과 매핑
                              // 생략 시 클래스 이름과 동일한 이름에 매핑
                              // 속성
                              // name: 테이블 이름
                              // catalog: 카탈로그 이름 ex) MySQL의 DB 이름
                              // schema: 스키마 이름 ex) Oracle의 schema 이름
                              // ex) @Table(catalog = "point", name = "point_history")
@Getter                       // 프로퍼티 접근 방식. 필드 접근 방식도 존재함
@Setter
@Builder                      // 빌더 패턴 코드
                              // 생성자 상단에 선언시 생성자에 포함된 필드만 빌더에 포함
@AllArgsConstructor           // 모든 필드 값을 파라미터로 받는 생성자 생성
@NoArgsConstructor            // 파라미터가 없는 디폴트 생성자 생성
                              // JPA는 public, protected의 기본 생성자 필수
                              // Hibernate는 private도 가능하긴 함
public class User {

    @Id                        // 식별자(identifier)에 대응
    @Column(name = "user_id")  // user_id 칼럼과 매핑
                               // 필드 이름이 카멜 케이스일 경우, 테이블 컬럼 이름을 언더 스코어로 자동 변환
                               // 컬럼명을 생략하면 필드명을 컬럼명으로 사용
    @GeneratedValue(strategy = GenerationType.IDENTITY)    // 기본 키(pk) 생성을 DB에 위임
                                                           // 식별 칼럼 방식으로, insert 쿼리가 실행되어야 값 확인 가능
    private Long userId;       // 속성은 final로 설정 시 저장이 불가함

    @Column(name = "username", length = 50, unique = true) // 객체 필드를 테이블 컬럼에 매핑
                                                           // nullable: null값의 허용 여부 설정 (기본 true)
                                                           // unique: unique 제약조건
                                                           // length: 문자 길이 제약조건, String Type에만 사용 (기본 255)
    private String username;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "nickname", length = 50)
    private String nickname;

    @Column(name = "activated")
    private boolean activated;

    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;
}