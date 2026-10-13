package com.spring.springbootapplication.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginForm {
  @NotBlank(message = "メールアドレスは必ず入力してください")
  @Size(max = 255, message = "メールアドレスは255文字以内で入力してください")
  @Email(message = "メールアドレスが正しい形式ではありません")
  private String email;

  @NotBlank(message = "パスワードは必ず入力してください")
  @Size(max = 255, message = "パスワードは255文字以内で入力してください")
  private String password;

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
}
