# Veritas Backend

Veritas는 학원 시설 및 강의 관리 시스템의 백엔드 애플리케이션입니다. 강의실/열람실 출입 관리, 강의 출석 관리, 시설 예약 등 다양한 기능을 제공합니다.

## 주요 기능

### 🔐 인증 시스템
- 카카오 소셜 로그인
- 일반 로그인 (이메일/전화번호 인증)
- 세션 기반 인증
- 관리자 권한 관리

### 📚 강의 관리
- 강의 생성, 수정, 삭제
- 강의 스케줄 관리
- 수강 신청 관리
- 강의 출석 자동 처리 (스케줄러 기반)

### 🏢 시설 관리
- **강의실 관리**: 강의실 정보 및 출입 기록 관리
- **열람실 관리**: 열람실 및 좌석 정보 관리
- 좌석 예약 및 출입 시스템

### 🚪 출입 관리
- 강의실 입/퇴실 기록
- 열람실 입/퇴실 기록
- 출입 이력 조회

### 📱 디바이스 관리
- 키오스크 디바이스 관리
- 출입 디바이스 관리

### 👥 학생 관리
- 학생 정보 관리
- 학생별 출석 이력 조회

## 기술 스택

- **언어**: Kotlin 1.9.23
- **프레임워크**: Spring Boot 3.2.4
- **보안**: Spring Security
- **데이터베이스**: MariaDB
- **ORM**: Spring Data JPA (Hibernate)
- **스케줄링**: Spring Scheduling
- **빌드 도구**: Gradle (Kotlin DSL)
- **Java 버전**: 17

## 주요 의존성

- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- Spring Boot Starter Quartz
- Jackson Kotlin Module
- MariaDB JDBC Driver
- Kotlinx Coroutines
- Lombok

## 프로젝트 구조

```
src/main/kotlin/com/oxingaxin/veritas/
├── access/          # 출입 관리
│   ├── controller/
│   ├── domain/
│   ├── repository/
│   └── service/
├── auth/            # 인증 및 권한
│   ├── controller/
│   ├── domain/
│   ├── repository/
│   ├── service/
│   └── SecurityConfig.kt
├── common/          # 공통 기능
│   ├── argumentresolver/
│   ├── exception/
│   ├── middleware/
│   ├── scheduler/
│   └── util/
├── device/          # 디바이스 관리
├── facility/        # 시설 관리
├── lecture/         # 강의 관리
└── student/         # 학생 관리
```

## 실행 방법

### 로컬 개발 환경

1. **프로젝트 클론**
```bash
git clone <repository-url>
cd veritas
```

2. **데이터베이스 설정**
   - MariaDB 설치 및 실행
   - `application.yml` 파일에서 데이터베이스 연결 정보 설정

3. **애플리케이션 실행**
```bash
# Windows
./gradlew.bat bootRun

# Linux/Mac
./gradlew bootRun
```

### Docker Compose를 이용한 실행

```bash
docker-compose up -d
```

다음 서비스들이 실행됩니다:
- **veritas-be**: Spring Boot 애플리케이션 (포트 8080)
- **mariadb**: MariaDB 데이터베이스 (포트 3306)
- **nginx**: 리버스 프록시 (포트 80, 443)
- **certbot**: SSL 인증서 자동 갱신

## API 엔드포인트

### 인증 (`/api/auth`)
- `POST /api/auth/login` - 일반 로그인
- `POST /api/auth/kakao/token` - 카카오 로그인
- `GET /api/auth/check` - 세션 확인
- `POST /api/auth/logout` - 로그아웃

### 강의 (`/api/lectures`)
- `GET /api/lectures` - 강의 목록 조회
- `POST /api/lectures` - 강의 생성
- `GET /api/lectures/{lectureId}` - 강의 상세 조회
- `PUT /api/lectures/{lectureId}` - 강의 수정
- `DELETE /api/lectures/{lectureId}` - 강의 삭제
- `GET /api/lectures/{lectureId}/schedules` - 강의 스케줄 조회
- `POST /api/lectures/{lectureId}/schedules` - 스케줄 생성

### 출입 (`/api/access`)
- `GET /api/access` - 전체 출입 기록 조회
- `GET /api/access/my` - 내 출입 기록 조회
- `POST /api/access/readingroom/enter` - 열람실 입실
- `POST /api/access/readingroom/exit` - 열람실 퇴실
- `POST /api/access/lectureroom/enter` - 강의실 입실
- `POST /api/access/lectureroom/exit` - 강의실 퇴실

### 열람실 (`/api/readingrooms`)
- `GET /api/readingrooms` - 열람실 목록 조회
- `POST /api/readingrooms` - 열람실 생성
- `GET /api/readingrooms/{roomId}` - 열람실 상세 조회
- `POST /api/readingrooms/{roomId}/seats` - 좌석 생성

### 디바이스 (`/api/devices`)
- `GET /api/devices/kiosks` - 키오스크 목록 조회
- `POST /api/devices/kiosks` - 키오스크 등록
- `GET /api/devices/entryDevices` - 출입 디바이스 목록 조회
- `POST /api/devices/entryDevices` - 출입 디바이스 등록

## 스케줄러

애플리케이션은 다음과 같은 스케줄러를 실행합니다:

- **출석 자동 처리**: 매시간 0분, 30분마다 실행 (강의 출석 자동 기록)
- **자정 처리**: 매일 자정 실행 (일일 초기화 작업)

## 설정 파일

### `application.yml`
- 데이터베이스 연결 설정
- JPA 설정
- 로깅 설정
- 세션 타임아웃 설정 (72시간)

### `docker-compose.yml`
- 컨테이너 오케스트레이션 설정
- 네트워크 구성
- 볼륨 마운트 설정

## 빌드 및 배포

### JAR 파일 빌드
```bash
./gradlew build
```

생성된 JAR 파일: `build/libs/veritas-0.0.1-SNAPSHOT.jar`

### Docker 이미지 빌드
```bash
docker build -t oxingaxin/veritas-be:latest .
```

## 개발 환경

- **JDK**: 17 이상
- **Gradle**: 8.x (Wrapper 포함)
- **Docker**: 20.x 이상 (선택사항)
- **Docker Compose**: 2.x 이상 (선택사항)
