const checkedImgPath = '/img/checked.png';
const uncheckedImgPath = '/img/unchecked.png';

const checkFalg = {
    id : false,
    pw : false,
    name : false,
    email : false,
    phone : false 
}

async function idCheckRequest(id){
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

// 최종 검사
function checkData() {
    if(!checkFalg["id"]){
        alert("아이디 중복 확인이 필요합니다.");
        return false;
    }
    if(!checkFalg["pw"]){
        alert("생성할 수 없는 형태의 비밀번호입니다.");
        return false;
    }
    if(!checkFalg["name"]){
        alert("생성할 수 없는 형태의 닉네임입니다.");
        return false;
    }
    if(!checkFalg["email"]){
        alert("유효하지 않은 형태의 이메일입니다.");
        return false;
    }
    if(!checkFalg["phone"]){
        alert("유효하지 않은 형태의 전화번호입니다.");
        return false;
    }

    return true;
}


// 계정 생성 요청
function createAccount() {
    if(checkData() === false){
        return;
    }

    const email = $('#email').val() + '@' + $('#email-domain').val();
    const phone = $('#phone').val() + '-' +
                  $('#phone2').val() + '-' +
                  $('#phone3').val();

    data = {
        userId : $('#id').val(),
        pw : $('#pw').val(),
        name : $('#nickname').val(),
        email : email,
        phone : phone
    }
    
    $.ajax({
        url: '/account/member',
        method: 'POST',
        contentType: 'application/json',
        dataType: 'json',
        data: JSON.stringify(data),
        success: function(data) {
            const responseData = data["data"];
            if(responseData["message"]){
                alert("회원가입이 완료되었습니다.");
                $(location).attr('href', '/login');
            }else{
                console.error(data["error"]);
                return;
            }
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