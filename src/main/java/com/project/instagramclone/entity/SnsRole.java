package com.project.instagramclone.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "SnsRole")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SnsRole {

    @Id
    @Column(name = "snsId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int snsId;

    @Column(name = "snsName", length = 10, unique = true)
    private String snsName;

}
