package com.project.instagramclone.Repository;

import com.project.instagramclone.entity.SnsRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnsRoleRepository extends JpaRepository<SnsRole, Integer> {
    SnsRole findBySnsName(String snsName);
}
