# 1단계: Gradle 빌드 (Gradle + JDK 21)
FROM gradle:8.4-jdk21 AS builder

# Node.js 및 npm 설치
RUN apt-get update && \
    apt-get install -y nodejs npm && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app # 컨테이너의 작업 디렉토리를 /app으로 설정

# settings.gradle 파일이 최상위에 있으므로 복사합니다.
COPY settings.gradle . 
# 백엔드 프로젝트와 프론트엔드 프로젝트 전체를 각각의 폴더로 복사
COPY back ./back 
COPY front ./front

# 이제 최상위 작업 디렉토리(/app)에서 빌드를 실행합니다.
RUN gradle build -x test 

# 2단계: 실제 Spring Boot 실행 (JDK 21)
FROM eclipse-temurin:21-jdk
WORKDIR /app
# builder 스테이지에서 백엔드 빌드 결과는 /app/back/build/libs 에 있습니다.
COPY --from=builder /app/back/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]