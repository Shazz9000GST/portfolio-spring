package com.spring.springbootapplication.model;

import  java.time.LocalDateTime;

public class User {
  private Long id;
  private String userName;
  private String email;
  private String password;
  private String profileImage;
  private String userBio;
  private LocalDateTime createdDate;
  private LocalDateTime updatedDate;

  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  public String getProfileImage() {
    return profileImage;
  }
  public void setProfileImage(String profileImage) {
    this.profileImage = profileImage;
  }
  public String getUserBio() {
    return userBio;
  }
  public void setUserBio(String userBio) {
    this.userBio = userBio;
  }
  public LocalDateTime getCreatedDate() {
    return createdDate;
  }
  public void setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }
  public LocalDateTime getUpdatedDate() {
    return updatedDate;
  }
  public void setUpdatedDate(LocalDateTime updatedDate) {
    this.updatedDate = updatedDate;
  }
}
