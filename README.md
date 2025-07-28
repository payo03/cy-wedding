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
- **Cloudinary**: 이미지 저장 및 URL 제공

### 💰 Free Tier 자원 비교

| 서비스          | 리소스 종류                   | Free Tier 제공량 (2025 기준)                                                            | 초과 시 정책 및 제한 사항                                                                 |
|-----------------|-------------------------------|------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------|
| **Render**      | Web 서비스 (Docker 등)       | – RAM: 약 0.5 vCPU 기준<br>– 월 750 인스턴스 시간<br>– 유휴 시 15분 후 자동 슬립         | • 월 750시간 소진 시 Free 서비스 정지<br>• 외부 API 호출 등 과도한 서비스-initiated 트래픽 시 유료 플랜 필요 :contentReference[oaicite:1]{index=1} |
|                 | Postgres 데이터베이스         | – 저장공간: 1 GB 이내<br>– 만료: 생성 후 30일 사용 가능, 이후 14일 grace period          | • 30일 후 만료, 업그레이드 안 하면 데이터 삭제<br>• 백업 및 복원 기능 없음 :contentReference[oaicite:2]{index=2}      |
| **NeonDB**      | PostgreSQL Database           | – 저장공간: 0.5 GB-month (정규 + 아카이브)<br>– 월 191.9 compute hours<br>– 월 5 GB egress (전송량)<br>– 프로젝트 최대 10개 | • Compute >191.9시간 시 컴퓨트 중단<br>• Branch 비-default 컴퓨트는 최대 5시간 제한, 초과 시 중단 :contentReference[oaicite:3]{index=3} |
| **Cloudinary**  | 이미지 저장 및 CDN            | – 월 25 크레딧: 25 GB storage 또는 25 GB bandwidth 또는 25,000 transformations<br>– 최대 이미지 파일 용량 10 MB, 비디오 100 MB | • 크레딧 소진 시 업그레이드 권유<br>• 무료는 시간 제한 없음, 크레딧 범위 내에서 무제한 사용 가능 :contentReference[oaicite:4]{index=4} |


> ✅ 모든 수치는 2025년 기준 **Free Tier** 기준입니다. 변동 가능성이 있으므로 각 서비스의 공식 문서를 참고하세요.
