# Blog (Spring Boot)

Spring Boot 블로그 — 로그인, 회원가입, 글 CRUD (H2 DB)

## 요구 사항

- JDK 8 이상
- Maven 3.6+

## 실행

```bash
mvn spring-boot:run
```

- 로그인: http://localhost:8080/login
- 회원가입: http://localhost:8080/register
- H2 콘솔: http://localhost:8080/h2-console (JDBC: `jdbc:h2:file:./data/blogdb`, user: `sa`, password: 비움)

## 계정

| 구분 | 아이디 | 비밀번호 |
|------|--------|----------|
| 초기 관리자 (자동 생성) | admin | admin123 |
| 일반 회원 | `/register`에서 가입 | — |

## 기능

- 회원가입 / 로그인 / 로그아웃
- 게시글 작성 · 목록 · 상세 · 수정 · 삭제 (본인 글만 수정/삭제)
- H2 파일 DB (`./data/blogdb`)
