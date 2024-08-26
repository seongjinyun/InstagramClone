package com.project.instagramclone.repository.oauth2;

import com.project.instagramclone.entity.oauth2.IdpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdpRepository extends JpaRepository<IdpEntity, Integer> {

}
