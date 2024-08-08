package com.project.instagramclone.repository;

import com.project.instagramclone.entity.Sns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnsRepository extends JpaRepository<Sns, Integer> {

}
