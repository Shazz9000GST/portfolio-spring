package com.spring.springbootapplication.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProfileImageService {
  public String saveImage(MultipartFile file) throws IOException {
    // 画像が選択されていない場合
    if (file == null || file.isEmpty()) {
      return null;
    }

    // ファイル形式を確認
    String extension = switch (file.getContentType()) {
      case "image/jpeg" -> "jpg";
      case "image/png" -> "png";
      case "image/gif" -> "gif";
      case "image/webp" -> "webp";
      default -> throw new IllegalArgumentException("対応していない形式です");
    };

    // 保存先のディレクトリ
    Path uploadDir = Paths.get("uploads", "profile-images")
                          .toAbsolutePath()
                          .normalize();
    
    Files.createDirectories(uploadDir);

    // 重複しないファイル名を生成
    String fileName = UUID.randomUUID() + "." + extension;

    // 画像を保存
    Path filePath = uploadDir.resolve(fileName);
    file.transferTo(filePath);

    // DBに保存するパスを返す
    return "/uploads/profile-images/" + fileName;
  }
}
