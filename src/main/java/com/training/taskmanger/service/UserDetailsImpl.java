package com.training.taskmanger.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.training.taskmanger.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

public class UserDetailsImpl implements UserDetails {
  private static final long serialVersionUID = 1L;

  private int id;

  private String username;

  private String email;

  private String name;

  @JsonIgnore
  private String password;

  private static Collection<? extends GrantedAuthority> authorities;

  public UserDetailsImpl(int id, String username, String email, String password,String name,
      Collection<? extends GrantedAuthority> authorities) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.password = password;
    this.name = name;
    this.authorities = authorities;
  }

  public static UserDetailsImpl build(User user) {

    return new UserDetailsImpl(
            user.getId(),
        user.getUsername(), 
        user.getEmail(),
        user.getPassword(),
            user.getName(),
            authorities);
  }

  public UserDetailsImpl() {
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  public int getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    boolean status = true;
    if(o==null)
      status = false;
  return status;
  }
}
