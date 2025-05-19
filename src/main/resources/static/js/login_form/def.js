// 해당 아이디가 데이터베이스에 존재하는지 확인
async function isIdExist(id){
    return $.ajax({
        url: `/account/${id}`,
        method: 'GET',
        dataType: 'json',
        success: function(data) {
            if(data["meaaage"] === false){
                console.error("서버측에서 처리 오류가 발생했습니다.\n" + data["error"]);
            }
            return data;
        },
        error: function(xhr, status, error) {
            alert(`요청 중 에러가 발생했습니다.\n\n${status}, ${error}`);
        }
    });
}

// 아이디와 비밀번호가 일치하는 계정이 있는지 확인
async function idPwMatched(id,pw){
    return $.ajax({
        url: '/account/check',
        method: 'POST',
        contentType: 'application/json',
        dataType: 'json',
        data: JSON.stringify({id:id, pw:pw}),
        success: function(data) {
            if(data["meaaage"] === false){
                console.error("서버측에서 처리 오류가 발생했습니다.\n" + data["error"]);
            }
            return data;
        },
        error: function(xhr, status, error) {
            alert(`요청 중 에러가 발생했습니다.\n\n${status}, ${error}`);
        }
    });
}

const login = () => {
    const $id = $('#id');
    const $pw = $('#password');

    if($id.val().trim() === ''){
        alert('아이디를 입력해주세요');
    }else if($pw.val().trim() === ''){
        alert('비밀번호를 입력해주세요');
    }else{
        isIdExist($id.val()).then(result => {
            if(result["data"]){
                idPwMatched($id.val(), $pw.val()).then(result => {
                    if(result["data"]){
                        $('#login-form').submit();
                        alert('로그인 되었습니다.')
                    }else{
                        alert('비밀번호 입력이 잘못되었습니다.\n다시 입력해주세요.');
                        return;
                    }
                });
            }else{
                alert('입력하신 아이디가 존재하지 않습니다.');
                return;
            }
        });
    }
}