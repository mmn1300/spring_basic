document.addEventListener('DOMContentLoaded', () => {
    // 게시글 칸 생성
    const tbody = document.querySelector('#post-content');
    for(let i=0; i<maxRow; i++){
        tbody.prepend(create_table_row());
    }

    // 사용자 로그인 상태 화면에 나타냄
    const loginState = document.querySelector('.login-state');
    checkLogin().then(response => {
        if(response["message"]){
            loginState.textContent = `${response['nickname']} 님 (${response['id'].slice(0,4)}****)`;
        }else{
            loginState.textContent = '로그인 되어있지 않음';
        }
    });

    contentLoad(1);
});
