# 1단계: Gradle 빌드 (Gradle + JDK 21)
FROM gradle:8.4-jdk21 AS builder

# Node.js 및 npm 설치 (앞서 추가했던 내용)
RUN apt-get update && \
    apt-get install -y nodejs npm && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app # 컨테이너의 작업 디렉토리를 /app으로 설정

# 모노레포의 back과 front 폴더를 각각 /app/back, /app/front로 복사
COPY back ./back
COPY front ./front

# 이제 백엔드 프로젝트 디렉토리로 이동하여 빌드 실행
WORKDIR /app/back
RUN gradle build -x test 

# 2단계: 실제 Spring Boot 실행 (JDK 21)
FROM eclipse-temurin:21-jdk
WORKDIR /app
# builder 스테이지에서 백엔드 빌드 결과는 /app/back/build/libs 에 있습니다.
COPY --from=builder /app/back/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]