# ===== Spring Bootをビルド =====
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

COPY . .

RUN sed -i 's/\r$//' gradlew \
    && chmod +x gradlew \
    && ./gradlew clean bootJar --no-daemon


# ===== 実行用コンテナ =====
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/build/libs/spring-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 10000

CMD ["java", "-jar", "app.jar"]