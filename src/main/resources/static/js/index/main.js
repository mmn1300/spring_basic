$(document).ready(() => {

    // 로그인 여부 검사
    checkLogin().then(response => {
        const responseData = response["data"];
        if(responseData["message"]){
            // 로그인 되어있을 시 로그인 상태 변경
            const $userInfo = $('#login-info');
            const $a = $(`<a id="${$userInfo.attr("id")}" href="/account/info"></a>`);
            $a.text(`${responseData['nickname']} 님 (${responseData['id']})`);
            $userInfo.replaceWith($a);
            const $loginBtn = $('#login');
            $loginBtn.text('로그아웃');
        }
    });
});
