# 1단계: 빌드 환경 설정 (Multi-stage build 활용)
FROM gradle:jdk21 AS builder

WORKDIR /app
# Gradle 의존성 캐싱을 위해 필요한 파일 먼저 복사
COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle ./gradle
# 의존성 다운로드 (변경 없을 시 캐시 활용)
RUN gradle dependencies --no-daemon

# 소스 코드 복사
COPY src ./src

# 애플리케이션 빌드
RUN gradle bootJar --no-daemon

# 2단계: 실행 환경 설정
FROM openjdk:21-jdk-slim

WORKDIR /app
# 빌드 결과물(JAR) 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 애플리케이션 실행 포트 노출
EXPOSE 8080

# 컨테이너 시작 시 실행될 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]