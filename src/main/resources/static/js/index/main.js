$(document).ready(() => {

    // 로그인 여부 검사
    checkLogin().then(response => {
        if(response["message"]){
            // 로그인 되어있을 시 로그인 상태 변경
            const $userInfo = $('#login-info');
            const $a = $(`<a id="${$userInfo.attr("id")}" href="/account/info"></a>`);
            $a.text(`${response['nickname']} 님 (${response['id'].slice(0,4)}****)`);
            $userInfo.replaceWith($a);
            const $loginBtn = $('#login');
            $loginBtn.text('로그아웃');
        }
    });
});
