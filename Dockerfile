# 1단계: Gradle 빌드 (Gradle + JDK 21)
FROM gradle:8.4-jdk21 AS builder

# Node.js 및 npm 설치
RUN apt-get update && \
    apt-get install -y nodejs npm && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app # 컨테이너의 작업 디렉토리를 /app으로 설정 (모노레포의 루트)

# settings.gradle 파일이 최상위에 있으므로 복사합니다.
COPY settings.gradle . 
# 백엔드 프로젝트와 프론트엔드 프로젝트 전체를 각각의 폴더로 복사
COPY back ./back 
COPY front ./front

# ---- 디버깅을 위한 로그 메시지 추가 시작 ----
# /app 디렉토리의 내용을 확인합니다. (back, front, settings.gradle이 보여야 함)
RUN echo "--- Listing contents of /app ---" && ls -la /app/

# /app/front 디렉토리의 내용을 확인합니다. (package.json이 보여야 함)
RUN echo "--- Listing contents of /app/front ---" && ls -la /app/front/

# /app/front/package.json 파일의 내용을 출력합니다.
RUN echo "--- Contents of /app/front/package.json ---" && cat /app/front/package.json
# ---- 디버깅을 위한 로그 메시지 추가 끝 ----

# 프론트엔드 프로젝트 디렉토리로 이동하여 npm 종속성을 설치합니다.
# 이 단계에서 package.json에 명시된 'vite'를 포함한 모든 패키지가 설치됩니다.
WORKDIR /app/front
RUN npm install

# 다시 모노레포의 루트 디렉토리로 돌아와서 Gradle 빌드를 실행합니다.
WORKDIR /app
RUN gradle build -x test 

# 2단계: 실제 Spring Boot 실행 (JDK 21)
FROM eclipse-temurin:21-jdk
WORKDIR /app
# builder 스테이지에서 백엔드 빌드 결과는 /app/back/build/libs 에 있습니다.
COPY --from=builder /app/back/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]