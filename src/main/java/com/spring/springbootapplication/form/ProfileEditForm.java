package com.spring.springbootapplication.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class ProfileEditForm {
  @NotBlank(message = "自己紹介は50文字以上200文字以下で入力してください")
  @Size(
    min = 50,
    max = 200,
    message = "自己紹介は50文字以上200文字以下で入力してください"
  )
  private String userBio;
  private MultipartFile profileImageFile;

  public String getUserBio() {
    return userBio;
  }

  public void setUserBio(String userBio) {
    this.userBio = userBio;
  }

  public MultipartFile getProfileImageFile() {
    return profileImageFile;
  }

  public void setProfileImageFile(MultipartFile profileImageFile) {
    this.profileImageFile = profileImageFile;
  }
}
