// 해당 아이디가 데이터베이스에 존재하는지 확인
async function isIdExist(id){
    return $.ajax({
        url: `/account/${id}`,
        method: 'GET',
        dataType: 'json',
        success: function(data) {
            return data;
        },
        error: function(xhr, status, error) {
            alert(`요청 중 에러가 발생했습니다.\n\n${status}, ${error}`);
            if (xhr.status === 500) {
                try{
                    const response = JSON.parse(xhr.responseText);
                    const responseData = response["data"];
                    if(responseData["meaaage"] === false){
                        console.error("서버측에서 처리 오류가 발생했습니다.\n" + responseData["error"]);
                    }
                }catch(e){
                    console.error('응답 파싱 실패:', e);
                }
            }
        }
    });
}

// 아이디와 비밀번호가 일치하는 계정이 있는지 확인
async function idPwMatched(id,pw){
    const csrfToken = $("meta[name='_csrf']").attr("content");

    return $.ajax({
        url: '/account/check',
        method: 'POST',
        headers: {
            "X-XSRF-TOKEN": csrfToken
        },
        contentType: 'application/json',
        dataType: 'json',
        data: JSON.stringify({username:id, password:pw}),
        success: function(data) {
            return data;
        },
        xhrFields: {
            withCredentials: true
        },
        error: function(xhr, status, error) {
            alert(`요청 중 에러가 발생했습니다.\n\n${status}, ${error}`);
            if (xhr.status === 500) {
                try{
                    const response = JSON.parse(xhr.responseText);
                    const responseData = response["data"];
                    if(responseData["meaaage"] === false){
                        console.error("서버측에서 처리 오류가 발생했습니다.\n" + responseData["error"]);
                    }
                }catch(e){
                    console.error('응답 파싱 실패:', e);
                }
            }
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
    }else if($id.val().length < 8 || $id.val().length > 15){
        alert('아이디 입력이 잘못되었습니다.\n다시 입력해주세요.');
        return;
    }else if($pw.val().length < 8 || $pw.val().length > 15){
        alert('비밀번호 입력이 잘못되었습니다.\n다시 입력해주세요.');
        return;
    }else{
        isIdExist($id.val()).then(result => {
            const idResultData = result["data"];
            if(idResultData["message"] && idResultData["data"]){
                idPwMatched($id.val(), $pw.val()).then(result => {
                    const accountResultData = result["data"];
                    if(accountResultData["message"] && accountResultData["data"]){
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