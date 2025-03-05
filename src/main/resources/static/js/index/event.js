document.addEventListener('DOMContentLoaded', () => {
    // 글쓰기 버튼 클릭시 GET 요청
    document.querySelector('#board').addEventListener('click', () => {
        window.location.href = '/board';
    });

    // 로그인 및 로그아웃 요청
    document.querySelector('#login').addEventListener('click', (e) => {
        if(e.target.textContent === '로그인'){
            window.location.href = '/login';
        }else{
            logout();
        }
    });
});