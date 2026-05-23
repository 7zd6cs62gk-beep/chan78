# My Blog — Spring Boot 블로그

Spring Boot 기반의 간단한 블로그 웹 애플리케이션입니다.  
회원가입·로그인, 게시글 CRUD, H2 파일 데이터베이스를 지원합니다.

**GitHub:** https://github.com/7zd6cs62gk-beep/chan78

---

## 목차

- [주요 기능](#주요-기능)
- [기술 스택](#기술-스택)
- [프로젝트 구조](#프로젝트-구조)
- [시작하기](#시작하기)
- [URL 및 화면](#url-및-화면)
- [계정 정보](#계정-정보)
- [데이터베이스](#데이터베이스)
- [API / 라우팅](#api--라우팅)
- [보안](#보안)
- [GitHub 연동](#github-연동)
- [문제 해결](#문제-해결)

---

## 주요 기능

| 기능 | 설명 |
|------|------|
| **회원가입** | 아이디, 이메일, 비밀번호로 가입 (중복 검사) |
| **로그인 / 로그아웃** | Spring Security 폼 로그인, 세션 기반 |
| **게시글 목록** | 최신순 정렬, 미리보기 표시 |
| **게시글 작성** | 제목·내용 입력 후 등록 |
| **게시글 상세** | 전체 내용 조회 |
| **게시글 수정·삭제** | **작성자 본인만** 가능 |
| **초기 관리자** | 앱 최초 실행 시 `admin` 계정 자동 생성 |

---

## 기술 스택

| 구분 | 기술 |
|------|------|
| Language | Java 8 |
| Framework | Spring Boot 2.7.18 |
| Security | Spring Security 5 |
| View | Thymeleaf |
| ORM | Spring Data JPA (Hibernate) |
| Database | H2 (파일 모드) |
| Build | Maven |
| Validation | Bean Validation (`@Valid`) |

---

## 프로젝트 구조

```
src/main/java/com/blog/
├── BlogApplication.java              # 메인 진입점
├── config/
│   ├── SecurityConfig.java           # 로그인·권한·CSRF 설정
│   └── DataInitializer.java          # admin 계정 초기화
├── controller/
│   ├── AuthController.java           # 로그인 페이지
│   ├── RegisterController.java       # 회원가입
│   ├── HomeController.java           # 홈(글 목록)
│   └── PostController.java           # 글 CRUD
├── dto/
│   ├── RegisterForm.java
│   └── PostForm.java
├── entity/
│   ├── Member.java                   # 회원
│   ├── Post.java                     # 게시글
│   └── Role.java                     # USER / ADMIN
├── repository/
│   ├── MemberRepository.java
│   └── PostRepository.java
├── security/
│   └── CustomUserDetailsService.java # DB 기반 인증
└── service/
    ├── MemberService.java
    └── PostService.java

src/main/resources/
├── application.properties
├── static/css/login.css
└── templates/
    ├── login.html
    ├── register.html
    ├── home.html
    ├── post-form.html
    └── post-detail.html
```

---

## 시작하기

### 요구 사항

- **JDK 8 이상** (JRE만으로는 빌드 불가)
- **Maven 3.6+**

### 설치 및 실행

```bash
# 저장소 클론
git clone https://github.com/7zd6cs62gk-beep/chan78.git
cd chan78

# 실행
mvn spring-boot:run
```

JAR로 실행:

```bash
mvn package -DskipTests
java -jar target/blog-login-1.0.0.jar
```

브라우저에서 접속: **http://localhost:8080/login**

---

## URL 및 화면

| URL | 설명 | 인증 |
|-----|------|------|
| `/` | `/login`으로 리다이렉트 | 불필요 |
| `/login` | 로그인 | 불필요 |
| `/register` | 회원가입 | 불필요 |
| `/home` | 게시글 목록 | 필요 |
| `/posts/new` | 글 작성 | 필요 |
| `/posts/{id}` | 글 상세 | 필요 |
| `/posts/{id}/edit` | 글 수정 | 필요 (작성자) |
| `/h2-console` | H2 DB 콘솔 | 불필요* |

\* 개발용으로 Security에서 허용됨. 운영 환경에서는 비활성화 권장.

---

## 계정 정보

### 초기 관리자 (자동 생성)

애플리케이션 **최초 실행 시** DB에 없으면 자동으로 생성됩니다.

| 아이디 | 비밀번호 | 역할 |
|--------|----------|------|
| `admin` | `admin123` | ADMIN |

### 일반 회원

`/register` 페이지에서 직접 가입합니다.

- 아이디: 3~50자
- 비밀번호: 6자 이상
- 이메일: 형식 검증 및 중복 불가

---

## 데이터베이스

### H2 파일 DB

- 저장 위치: 프로젝트 루트 `./data/blogdb`
- 재시작 후에도 데이터 유지
- `spring.jpa.hibernate.ddl-auto=update` — 엔티티 변경 시 스키마 자동 반영

### H2 콘솔 접속

1. http://localhost:8080/h2-console
2. 설정값:

| 항목 | 값 |
|------|-----|
| JDBC URL | `jdbc:h2:file:./data/blogdb` |
| User Name | `sa` |
| Password | *(비움)* |

### ERD (개념)

```
members (1) ──────< (N) posts
  - id
  - username (unique)
  - password (BCrypt)
  - email
  - role
  - created_at
```

---

## API / 라우팅

### 인증

| Method | Path | 처리 |
|--------|------|------|
| GET | `/login` | 로그인 폼 |
| POST | `/login` | Spring Security 로그인 처리 |
| POST | `/logout` | 로그아웃 |

### 회원

| Method | Path | 처리 |
|--------|------|------|
| GET | `/register` | 가입 폼 |
| POST | `/register` | 가입 처리 → `/login?registered` |

### 게시글

| Method | Path | 처리 |
|--------|------|------|
| GET | `/home` | 목록 |
| GET | `/posts/new` | 작성 폼 |
| POST | `/posts` | 등록 |
| GET | `/posts/{id}` | 상세 |
| GET | `/posts/{id}/edit` | 수정 폼 |
| POST | `/posts/{id}/edit` | 수정 |
| POST | `/posts/{id}/delete` | 삭제 |

---

## 보안

- 비밀번호: **BCrypt** 해싱 저장
- 로그인 실패 시: `/login?error`
- CSRF: Thymeleaf 폼에 토큰 포함
- 글 수정·삭제: 서비스 레이어에서 작성자 ID 검증
- `data/`, `target/` — `.gitignore`에 포함 (DB·빌드 산출물 미업로드)

---

## GitHub 연동

```bash
git remote add origin https://github.com/7zd6cs62gk-beep/chan78.git
git push -u origin main
```

또는 프로젝트 루트의 `connect-github.ps1` 스크립트 사용:

```powershell
.\connect-github.ps1 -Username "7zd6cs62gk-beep" -RepoName "chan78"
```

---

## 문제 해결

### `No compiler is provided` / JRE만 설치됨

→ [Eclipse Temurin JDK](https://adoptium.net/) 8 이상 설치 후 `JAVA_HOME` 설정

### 포트 8080 사용 중

`application.properties`에서 변경:

```properties
server.port=8081
```

### 로그인이 안 됨

- DB 초기화 후: `./data` 폴더 삭제 후 재실행 → `admin` / `admin123` 재생성
- 회원가입 계정은 `/register`에서 만든 아이디 사용

### CSS 변경이 안 보임

브라우저 **강력 새로고침** (`Ctrl + F5`)

---

## 라이선스

이 프로젝트는 학습·포트폴리오 용도로 자유롭게 사용할 수 있습니다.
