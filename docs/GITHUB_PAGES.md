# GitHub Pages 블로그 활성화

## 1. Pages 켜기

1. https://github.com/7zd6cs62gk-beep/chan78/settings/pages
2. **Build and deployment** → Source: **Deploy from a branch**
3. Branch: **main** / Folder: **/docs**
4. **Save**

## 2. 접속 URL

몇 분 후 아래 주소에서 블로그가 열립니다.

**https://7zd6cs62gk-beep.github.io/chan78/**

- 홈: `index.html`
- 이력: `resume.html`

## 3. 이력 수정하기

`docs/resume.json` 파일을 GitHub에서 직접 편집합니다.

```json
"experience": [
  {
    "period": "2025.01 – 현재",
    "role": "직무/역할",
    "company": "회사 또는 프로젝트명",
    "description": "한 줄 설명"
  }
]
```

- `education` — 학력
- `certificates` — 자격증
- `posts` — 블로그 글 목록 (홈 화면)
- `skills` — 기술 스택 칩
- `profile` — 이름, 소개, 연락처

저장(커밋) 후 1~2분 뒤 사이트에 반영됩니다.
