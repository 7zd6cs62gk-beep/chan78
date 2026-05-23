function getBasePath() {
    const base = document.querySelector('base');
    if (base && base.href) {
        return new URL(base.href).pathname;
    }
    const path = window.location.pathname;
    if (path.endsWith('.html')) {
        return path.substring(0, path.lastIndexOf('/') + 1);
    }
    return path.endsWith('/') ? path : path + '/';
}

async function loadResume() {
    const res = await fetch(getBasePath() + 'resume.json');
    if (!res.ok) {
        throw new Error('resume.json을 불러올 수 없습니다.');
    }
    return res.json();
}

function renderProfile(data, containerId) {
    const p = data.profile;
    const links = (p.links || [])
        .map(l => `<a href="${l.url}" target="_blank" rel="noopener">${l.label}</a>`)
        .join(' · ');

    document.getElementById(containerId).innerHTML = `
        <div class="hero">
            <h1>${p.name}</h1>
            <p class="title">${p.title}</p>
            <p class="summary">${p.summary}</p>
            <p class="meta">${p.email} · ${p.location}</p>
            <p class="meta">${links}</p>
            <div class="chip-list">
                ${(data.skills || []).map(s => `<span class="chip">${s}</span>`).join('')}
            </div>
        </div>
    `;
}

function renderTimeline(items, containerId, titleFn, orgFn) {
    const el = document.getElementById(containerId);
    if (!items || items.length === 0) {
        el.innerHTML = '<p style="color:#64748b">등록된 항목이 없습니다. resume.json을 수정해 추가하세요.</p>';
        return;
    }
    el.innerHTML = '<ul class="timeline">' + items.map(item => `
        <li>
            <div class="period">${item.period}</div>
            <h3>${titleFn(item)}</h3>
            <div class="org">${orgFn(item)}</div>
            <p>${item.description || ''}</p>
        </li>
    `).join('') + '</ul>';
}

function renderPosts(posts) {
    const el = document.getElementById('posts');
    if (!posts || posts.length === 0) {
        el.innerHTML = '<p style="color:#64748b">게시글이 없습니다.</p>';
        return;
    }
    el.innerHTML = posts.map(post => `
        <article class="post-item">
            <div class="date">${post.date}</div>
            <h3>${post.title}</h3>
            <p>${post.summary}</p>
            <div class="chip-list">
                ${(post.tags || []).map(t => `<span class="chip">${t}</span>`).join('')}
            </div>
        </article>
    `).join('');
}

function renderCertificates(items) {
    const el = document.getElementById('certificates');
    if (!items || items.length === 0) {
        el.parentElement.style.display = 'none';
        return;
    }
    renderTimeline(
        items.map(c => ({
            period: c.period,
            description: c.issuer,
            role: c.name,
            company: ''
        })),
        'certificates',
        item => item.role,
        () => ''
    );
}

async function initPage() {
    try {
        const data = await loadResume();
        if (document.getElementById('profile')) {
            renderProfile(data, 'profile');
        }
        if (document.getElementById('experience')) {
            renderTimeline(
                data.experience,
                'experience',
                item => item.role,
                item => item.company
            );
        }
        if (document.getElementById('education')) {
            renderTimeline(
                data.education,
                'education',
                item => item.major,
                item => item.school
            );
        }
        if (document.getElementById('certificates')) {
            renderCertificates(data.certificates);
        }
        if (document.getElementById('posts')) {
            renderPosts(data.posts);
        }
    } catch (e) {
        console.error(e);
        const main = document.querySelector('.wrap');
        if (main) {
            const err = document.createElement('div');
            err.className = 'card';
            err.innerHTML = `<p style="color:#b91c1c">데이터 로드 실패: ${e.message}</p>`;
            main.appendChild(err);
        }
    }
}

document.addEventListener('DOMContentLoaded', initPage);
