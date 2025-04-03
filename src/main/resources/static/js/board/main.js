$(document).ready(() => {
    // 게시글 칸 생성
    const tbody = $('#post-content');
    for(let i=0; i<maxRow; i++){
        tbody.prepend(createTableRow());
    }

    // 사용자 로그인 상태 화면에 나타냄
    const loginState = $('.login-state').eq(0);
    checkLogin().then(response => {
        if(response["message"]){
            loginState.text(`${response['nickname']} 님 (${response['id'].slice(0,4)}****)`);
        }else{
            loginState.tex('로그인 되어있지 않음');
        }
    });

    contentLoad(1);
});
