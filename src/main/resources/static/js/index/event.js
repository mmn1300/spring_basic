$(document).ready(() => {

    // 글쓰기 버튼 클릭시 GET 요청
    $('#board').on('click', () => {
        $(location).attr('href', '/board');
    });

    // 로그인 및 로그아웃 요청
    $('#login').on('click', (e) => {
        if($(e.currentTarget).text() === '로그인'){
            $(location).attr('href', '/login');
        }else{
            logout();
        }
    });
});