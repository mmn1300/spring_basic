$(document).ready(() => {
    // 게시글 칸 생성
    const $tbody = $('#post-content');
    for(let i=0; i<maxRow; i++){
        $tbody.prepend(createTableRow());
    }

    // 사용자 로그인 상태 화면에 나타냄
    const $loginState = $('.login-state').eq(0);
    checkLogin().then(response => {
        const responseData = response["data"];
        if(responseData["message"]){
            const $a = $('<a href="/account/info"></a>');
            $a.text(`${responseData['nickname']} 님 (${responseData['id']})`);
            $loginState.append($a);
        }else{
            $loginState.tex('로그인 되어있지 않음');
        }
    });

    if (searchOptions.userOption.id !== "null") {
        addSearchOption(`${searchOptions.userOption.userId.slice(1, -1)}`);
        contentLoadByUser(1, parseInt(searchOptions.userOption.id));
    }else{
        contentLoad(1);
    }
});
