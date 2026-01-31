# ---- build stage ----
FROM gradle:8.7-jdk17 AS builder
WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
RUN chmod +x ./gradlew

# 캐시용: 소스 없이 의존성 먼저 받아두기
RUN ./gradlew dependencies --no-daemon || true

COPY src ./src
RUN ./gradlew clean bootJar --no-daemon

# ---- run stage ----
FROM eclipse-temurin:17-jre
WORKDIR /app

# 보안/운영: non-root 유저로 실행
RUN useradd -m spring
USER spring

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
