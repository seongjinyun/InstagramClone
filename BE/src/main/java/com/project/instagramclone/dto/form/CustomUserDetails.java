package com.project.instagramclone.dto.form;

import com.project.instagramclone.entity.form.FormUserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
public class CustomUserDetails implements UserDetails  {

    private final FormUserEntity formUserEntity;

    public CustomUserDetails(FormUserEntity formUserEntity) {
        this.formUserEntity = formUserEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add((GrantedAuthority) () -> formUserEntity.getRole());
        return collection;
    }

    @Override
    public String getPassword() {
        return formUserEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return formUserEntity.getUsername();
    }

    public String getNickname() {
        return formUserEntity.getNickname();
    }

    public Long getMemberId() {
        return formUserEntity.getMemberEntity().getMemberId();
    }

    @Override
    public boolean isAccountNonExpired() {
        // return UserDetails.super.isAccountNonExpired();
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // return UserDetails.super.isAccountNonLocked();
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // return UserDetails.super.isCredentialsNonExpired();
        return true;
    }

    @Override
    public boolean isEnabled() {
        // return UserDetails.super.isEnabled();
        return true;
    }
}
