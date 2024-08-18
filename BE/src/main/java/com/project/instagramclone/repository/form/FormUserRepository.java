package com.project.instagramclone.repository.form;

import com.project.instagramclone.entity.form.FormUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FormUserRepository extends JpaRepository<FormUserEntity, Long> {

    Boolean existsByUsername(String username);
    Optional<FormUserEntity> findByUsername(String username);
    Optional<FormUserEntity> findByNickname(String nickname);

}
