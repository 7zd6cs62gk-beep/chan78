# GitHub 원격 저장소 연결 스크립트
# 사용법: .\connect-github.ps1 -Username "본인깃허브아이디" -RepoName "blog-login"

param(
    [Parameter(Mandatory = $true)]
    [string]$Username,

    [Parameter(Mandatory = $false)]
    [string]$RepoName = "blog-login"
)

$gitCandidates = @(
    "git",
    "$env:TEMP\MinGit\cmd\git.exe",
    "C:\Program Files\Git\bin\git.exe",
    "C:\Program Files\Git\cmd\git.exe"
)

$git = $null
foreach ($c in $gitCandidates) {
    if ($c -eq "git") {
        $cmd = Get-Command git -ErrorAction SilentlyContinue
        if ($cmd) { $git = $cmd.Source; break }
    } elseif (Test-Path $c) {
        $git = $c
        break
    }
}

if (-not $git) {
    Write-Host "Git을 찾을 수 없습니다. https://git-scm.com/download/win 에서 설치하세요." -ForegroundColor Red
    exit 1
}

Set-Location $PSScriptRoot
$remoteUrl = "https://github.com/$Username/$RepoName.git"

Write-Host "원격 URL: $remoteUrl" -ForegroundColor Cyan

$existing = & $git remote get-url origin 2>$null
if ($LASTEXITCODE -eq 0) {
    Write-Host "기존 origin: $existing"
    $answer = Read-Host "origin을 위 URL로 변경할까요? (y/n)"
    if ($answer -eq "y") {
        & $git remote set-url origin $remoteUrl
    }
} else {
    & $git remote add origin $remoteUrl
}

Write-Host ""
Write-Host "푸시 중... (GitHub 로그인 또는 Personal Access Token 필요)" -ForegroundColor Yellow
& $git push -u origin main

if ($LASTEXITCODE -eq 0) {
    Write-Host "완료! https://github.com/$Username/$RepoName" -ForegroundColor Green
} else {
    Write-Host ""
    Write-Host "푸시 실패 시 확인:" -ForegroundColor Yellow
    Write-Host "1. GitHub에서 빈 저장소를 먼저 만드세요: https://github.com/new"
    Write-Host "2. README 추가 옵션은 끄세요"
    Write-Host "3. 비밀번호 대신 Personal Access Token 사용"
}
