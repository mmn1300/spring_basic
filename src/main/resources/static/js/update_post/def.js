const pathSegments = window.location.pathname.split('/');
const pathVariable = pathSegments[pathSegments.length - 1];

// 로그인이 되어있는지 확인. 요청을 통해 로그인 되어있다면 아이디와 닉네임을 불러옴
async function checkLogin() {
    return fetch('/session', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP 오류. 상태코드: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        if(data['error'] !== undefined){
            console.log(data['error']);
        }
        return data;
    })
    .catch((error) => {
        alert(`요청 중 에러가 발생했습니다.\n\n${error.message}`);
    });
};

// 게시글을 수정하는 비동기 요청 함수
const updatePost = (postNum) => {
    const title = document.querySelector('#title-input').value;
    const content = document.querySelector('#content').value;
    if(title.trim() !== ''){
        if(content.trim() !== ''){
            update(postNum);
        }else{
            alert('내용을 입력해주세요.');
        }
    }else{
        alert('제목을 입력해주세요.');
    }
};

function update(postNum){
    const title = document.querySelector('#title-input').value;
    const content = document.querySelector('#content').value;

    fetch(`/board/update/${postNum}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({title:title, content:content})
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP 오류. 상태코드: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        if(data["message"]){
            alert("게시글을 성공적으로 수정하였습니다.");
            window.location.href = `/board/show/${postNum}`;
        }else{
            console.error(data["error"]);
        }
    })
    .catch((error) => {
        alert(`요청 중 에러가 발생했습니다.\n\n${error.message}`);
    });
}

function getCurrentTime() {
    const now = new Date();

    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');

    document.querySelector('#date-time').textContent = `${year}-${month}-${day} ${hours}:${minutes}`;
}