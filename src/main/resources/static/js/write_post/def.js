// 로그인이 되어있는지 확인. 요청을 통해 로그인 되어있다면 아이디와 닉네임을 불러옴
async function checkLogin() {
    return fetch('/account/session', {
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
        if(!data['meaaage']){
            console.error(data['error']);
        }
        return data;
    })
    .catch((error) => {
        alert(`요청 중 에러가 발생했습니다.\n\n${error.message}`);
    });
};

// 글을 작성하는 함수.
function writePostEvent() {
    const title = document.querySelector('#title-input').value;
    const content = document.querySelector('#content').value;
    if(title.trim() !== ''){
        if(content.trim() !== ''){
            writePost(title, content);
        }else{
            alert('내용을 입력해주세요.');
        }
    }else{
        alert('제목을 입력해주세요.');
    }
};

function writePost(title, content){
    fetch('/board/store', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ title:title, content:content })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP 오류. 상태코드: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        if(data["message"]){
            alert("게시글이 성공적으로 작성되었습니다.");
            window.location.href = '/board';
        }else{
            alert("게시글 작성을 실패하였습니다.");
            console.error(data["error"]);
        }
    })
    .catch((error) => {
        alert(`요청 중 에러가 발생했습니다.\n\n${error.message}`);
    });
};

function getCurrentTime() {
    const now = new Date();

    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');

    document.querySelector('#date-time').textContent = `${year}-${month}-${day} ${hours}:${minutes}`;
}