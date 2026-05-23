# GitHub Pages 블로그 활성화

## 현재 상태 확인

블로그 URL: **https://7zd6cs62gk-beep.github.io/chan78/**

| 증상 | 원인 | 해결 |
|------|------|------|
| **404 Not Found** | Pages 미설정 | 아래 1번 수행 |
| 스타일 없음 | CSS 경로 | `<base href="/chan78/">` 적용됨 — 재배포 후 새로고침 |
| 이력 안 보임 | `resume.json` 로드 실패 | F12 콘솔 확인, Pages 배포 완료 여부 확인 |

---

## 1. Pages 켜기 (필수)

### 방법 A — GitHub Actions (권장)

저장소에 `.github/workflows/pages.yml` 이 포함되어 있습니다.

1. https://github.com/7zd6cs62gk-beep/chan78/settings/pages
2. **Build and deployment** → Source: **GitHub Actions**
3. **Actions** 탭 → **Deploy GitHub Pages** 워크플로 실행 확인 (초록 체크)
4. Settings → Pages 에서 사이트 URL 확인

### 방법 B — Branch 배포

1. Settings → Pages
2. Source: **Deploy from a branch**
3. Branch: **main** / Folder: **/docs**
4. Save

---

## 2. 배포 후 접속

- 홈: https://7zd6cs62gk-beep.github.io/chan78/
- 이력: https://7zd6cs62gk-beep.github.io/chan78/resume.html

배포 완료까지 **1~5분** 걸릴 수 있습니다.

---

## 3. 이력 수정

[`docs/resume.json`](https://github.com/7zd6cs62gk-beep/chan78/edit/main/docs/resume.json) 편집 후 커밋 → 자동 재배포.
