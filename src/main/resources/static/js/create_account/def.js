const checkedImgPath = '/img/checked.png';
const uncheckedImgPath = '/img/unchecked.png';

const checkFalg = {
    id : false,
    pw : false,
    name : false,
    email : false,
    phone : false 
}

async function idCheck(id){
    return fetch(`/account/${id}`, {
        method: 'GET',
        dataType: 'json',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(data => {
        return data['message'];
    })
    // 동작이 정상적으로 수행되지 못했을 경우 에러 메세지를 출력함
    .catch(error => {
        alert('동작 수행 중 에러가 발생했습니다.');
        console.error('Error:', error);
    });
}

// 최종 검사 및 계정 생성 요청
async function createAccount() {
    if(!checkFalg["id"]){
        alert("아이디 중복 확인이 필요합니다.");
        return;
    }
    if(!checkFalg["pw"]){
        alert("생성할 수 없는 형태의 비밀번호입니다.");
        return;
    }
    if(!checkFalg["name"]){
        alert("생성할 수 없는 형태의 닉네임입니다.");
        return;
    }
    if(!checkFalg["email"]){
        alert("유효하지 않은 형태의 이메일입니다.");
        return;
    }
    if(!checkFalg["phone"]){
        alert("유효하지 않은 형태의 전화번호입니다.");
        return;
    }

    const email = document.querySelector('#email').value + '@' + document.querySelector('#email-domain').value;

    const phone = document.querySelector('#phone').value + '-' +
                  document.querySelector('#phone2').value + '-' +
                  document.querySelector('#phone3').value;

    data = {
        id : document.querySelector('#id').value,
        pw : document.querySelector('#pw').value,
        name : document.querySelector('#nickname').value,
        email : email,
        phone : phone
    }
    fetch('/signup', {
        method: 'POST',
        dataType: 'json',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(data => {
        if(data["message"]){
            alert("회원가입이 완료되었습니다.");
            window.location.href = '/login';
        }else{
            alert("로그인 중 문제가 발생하였습니다.");
            console.error(data["error"]);
            return;
        }
    })
    // 동작이 정상적으로 수행되지 못했을 경우 에러 메세지를 출력함
    .catch(error => {
        alert('동작 수행 중 에러가 발생했습니다.');
        console.error('Error:', error);
    });
}