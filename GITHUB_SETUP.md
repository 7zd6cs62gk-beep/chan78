# GitHub 연결 방법

로컬 Git 저장소는 이미 준비되어 있습니다. 아래 순서대로 GitHub에 올리면 됩니다.

## 1. Git 설치 (권장)

PC에 Git이 없으면 먼저 설치하세요.

- https://git-scm.com/download/win

설치 후 **새 터미널**을 열어야 `git` 명령이 동작합니다.

## 2. GitHub에서 저장소 만들기

1. https://github.com/new 접속
2. Repository name: `blog-login` (원하는 이름)
3. **README, .gitignore 추가하지 않기** (이미 로컬에 있음)
4. **Create repository** 클릭

## 3. 원격 저장소 연결 & 푸시

GitHub에서 만든 저장소 URL을 사용합니다. (`YOUR_USERNAME`을 본인 아이디로 바꾸세요)

```powershell
cd c:\Users\Administrator\Documents\my

git remote add origin https://github.com/YOUR_USERNAME/blog-login.git
git push -u origin main
```

로그인 창이 뜨면 GitHub 계정으로 인증합니다.  
(비밀번호 대신 **Personal Access Token**이 필요할 수 있습니다.)

### Personal Access Token 발급

1. GitHub → Settings → Developer settings → Personal access tokens
2. Generate new token (classic) → `repo` 권한 체크
3. `git push` 시 비밀번호 입력란에 **토큰** 붙여넣기

## 4. 연결 확인

```powershell
git remote -v
```

`origin` URL이 보이면 연결된 것입니다.

---

## Cursor에서 Git 사용

1. **Source Control** 패널 (왼쪽 분기 아이콘)
2. **Publish to GitHub** 또는 **Add Remote** 선택
3. 저장소 이름 입력 후 게시
