# TodoList Backend

Spring Boot 기반 TodoList REST API 서버

## 기술 스택

- **Framework**: Spring Boot 4.0.3 (Kotlin)
- **Language**: Kotlin 2.2.21 / Java 21
- **DB**: PostgreSQL 15 (AWS RDS)
- **ORM**: Spring Data JPA
- **인증**: Spring Security + JWT + Google OAuth2
- **파일 저장**: AWS S3 (SDK v2)
- **빌드**: Gradle Kotlin DSL

## 주요 기능

### 인증
- 일반 회원가입 / 로그인 (JWT 발급)
- Google OAuth2 로그인
- JWT 기반 API 인증

### Todo CRUD
- Todo 생성 / 조회 / 수정 / 삭제 (soft delete)
- 완료 토글 + 날짜별 완료 목록 조회
- Tiptap JSON 기반 설명(description) 저장
- 마감 시각(dueDate) 설정

### 이미지 업로드
- S3 이미지 업로드 (jpeg/png/gif/webp)
- Todo 첨부파일 관리 (추가/조회/삭제)
- Tiptap 에디터 인라인 이미지 삽입

### 회원
- 내 정보 조회 / 수정 / 탈퇴 (soft delete)

## API 엔드포인트

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/v1/auth/signup` | 회원가입 |
| POST | `/api/v1/auth/login` | 로그인 |
| GET | `/api/v1/users/me` | 내 정보 조회 |
| GET | `/api/v1/todos` | 미완료 Todo 목록 |
| GET | `/api/v1/todos/completed?date=` | 날짜별 완료 목록 |
| GET | `/api/v1/todos/{id}` | Todo 상세 |
| POST | `/api/v1/todos` | Todo 생성 |
| PUT | `/api/v1/todos/{id}` | Todo 수정 |
| PATCH | `/api/v1/todos/{id}/toggle` | 완료 토글 |
| DELETE | `/api/v1/todos/{id}` | Todo 삭제 |
| POST | `/api/v1/uploads/image` | 이미지 업로드 |
| GET | `/api/v1/todos/{id}/attachments` | 첨부 목록 |
| POST | `/api/v1/todos/{id}/attachments` | 첨부 추가 |
| DELETE | `/api/v1/todos/{id}/attachments/{aId}` | 첨부 삭제 |

## 배포

### 아키텍처

```
GitHub (main push)
  → GitHub Actions: Gradle 빌드 → JAR 생성
  → SCP로 EC2 전송
  → SSH로 systemd 서비스 재시작

EC2 (Amazon Linux)
  └── systemd (todolist.service)
      └── java -jar todolist.jar --spring.profiles.active=prod
          ├── RDS (PostgreSQL)
          └── S3 (이미지 저장)
```

### GitHub Actions 자동 배포
- `main` 브랜치에 push 시 자동 실행
- GitHub Secrets로 시크릿 관리 (DB 비밀번호, JWT, OAuth, AWS 키)
- 비시크릿 설정값은 `application-prod.yml`에 직접 정의

### 로컬 실행

```bash
# DB (Docker)
docker run -d --name todolist-db \
  -e POSTGRES_DB=todolistdb \
  -e POSTGRES_USER=todo \
  -e POSTGRES_PASSWORD=todo1234 \
  -p 5432:5432 postgres:15

# 프로젝트 루트에 local.env 파일 필요 (시크릿 키)
# GOOGLE_CLIENT_SECRET, AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY

# 서버 실행
./gradlew bootRun
```
