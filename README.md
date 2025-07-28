# 💍 Photo Event

하객이 직접 참여하는 **결혼식 사진 이벤트 플랫폼**입니다.  
결혼식장에서 QR코드를 스캔하면, **로그인 없이** 웹 페이지에 접속하여  
직접 사진을 업로드하고, 마음에 드는 사진에 투표할 수 있습니다.  
**Spring Boot + Vue.js** 기반으로 개발되었습니다.

![홈 화면](./HOME.png)

---

## 🔗 QR 생성 사이트

하객 참여용 QR은 다음 사이트에서 쉽게 생성할 수 있습니다:

👉 [https://genqrcode.com/ko/multiple](https://genqrcode.com/ko/multiple)

---

## ✨ 주요 기능

- 📸 **사진 업로드**  
  QR로 접속 후, 본인의 기기로 간단하게 사진을 업로드할 수 있습니다.  
  업로드된 이미지는 **Cloudinary**에 저장되며, 데이터베이스에는 이미지의 URL만 저장됩니다.

- 🎀 **사진 투표**  
  마음에 드는 순간을 투표로 뽑아주세요. 베스트 포토를 함께 만들어갑니다.

- 🔐 **로그인 불필요**  
  별도의 회원가입이나 로그인 없이 바로 참여할 수 있습니다.

---

## 🛠️ 기술 스택

| 구분      | 사용 기술          |
|-----------|-------------------|
| Backend   | Spring Boot       |
| Frontend  | Vue.js            |
| Build Tool| Gradle            |
| Infra     | Render, NeonDB, Cloudinary |

---

## 🗂️ 아키텍처 요약

- 사용자가 사진을 업로드하면, 이미지 파일은 **Cloudinary**에 저장되고  
- 응답받은 `secure_url`만 **NeonDB(PostgreSQL)** 에 저장됩니다.  
- 이미지 다운로드나 이메일 전송 시에는 해당 URL을 활용하여 Cloudinary에서 직접 불러옵니다.

---

## 🚀 로컬 실행 방법

```bash
# 프론트엔드 실행
cd front
npm install
npm run dev

# 백엔드 실행
cd back
./gradlew bootRun  # Windows는 gradlew.bat 사용
```

---

## ✅ TODO

- [ ] **구독제 기반 기능 개발**
  - ECO 플랜  
    1. [✔] 투표 상위 10장 메일로 전송  
    2. [✔] 사진 업로드 및 투표 1회  
  - Premium 플랜  
    1. [✔] 전체 업로드 사진을 메일로 전송  
    2. [ ] 사진 업로드 및 투표횟수 설정

- [ ] **병렬 결혼식 이벤트 처리**
  - 동시에 여러 결혼식 이벤트가 운영될 수 있도록 구조 개선  
  - 각 결혼식마다 고유 ID를 통해 업로드/투표 데이터 분리

---

## 📦 외부 서비스 사용 내역

- **Render**: Spring Boot 애플리케이션 호스팅  
- **NeonDB**: PostgreSQL 기반의 클라우드 데이터베이스  
- **Cloudinary**: 이미지 저장 및 URL 제공 (무료 플랜 사용 중)