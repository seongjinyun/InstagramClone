package com.project.instagramclone.entity.oauth2;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "idp")
@Getter
@Setter
@NoArgsConstructor
public class IdpEntity {

    @Id
    @Column(name = "idpId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idpId;

    @Column(name = "idpName")
    private String idpName;

    @Builder
    public IdpEntity(String idpName) {
        this.idpName = idpName;
    }

}
