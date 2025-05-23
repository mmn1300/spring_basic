const pathSegments = $(location).attr('pathname').split('/');
const pathVariable = pathSegments[pathSegments.length - 1];

// 로그인이 되어있는지 확인. 요청을 통해 로그인 되어있다면 아이디와 닉네임을 불러옴
async function checkLogin() {
    return $.ajax({
        url: '/session',
        method: 'GET',
        success: function(data) {
            return data;
        },
        error: function (xhr, textStatus, errorThrown) {
            console.error('파일 다운로드 오류:', textStatus, errorThrown);
        }
    });
};

// 게시글을 수정하는 비동기 요청 함수
const updatePost = (postNum) => {
    const title = $('#title-input').val();
    const content = $('#content').val();
    if (title.trim() !== '') {
        if (content.trim() !== '') {
            const file = $('#file-upload')[0].files[0];
            update(postNum, title, content, file);
        } else {
            alert('내용을 입력해주세요.');
        }
    } else {
        alert('제목을 입력해주세요.');
    }
};

function update (postNum, title, content, file) {
    const formData = new FormData();
    formData.append('title', title);
    formData.append('content', content);
    formData.append('file', file);

    $.ajax({
        url: `/board/post/${postNum}`,
        method: 'PUT',
        data: formData,
        contentType: false,
        processData: false,
        success: function(data) {
            const responseData = data["data"];
            if (responseData["message"]) {
                alert("게시글을 성공적으로 수정하였습니다.");
                window.location.href = `/board/show/${postNum}`;
            } else {
                console.error(responseData["error"]);
            }
        },
        error: function (xhr, textStatus, errorThrown) {
            console.error('게시글 업데이트 오류:', textStatus, errorThrown);
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

function getCurrentTime() {
    const now = new Date();

    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');

    $('#date-time').text(`${year}-${month}-${day} ${hours}:${minutes}`);
}

const changeText = () => {
    const fileInput = $('#file-upload')[0];
    const $fileLabel = $('.file-label').first();
    
    if (fileInput.files.length === 0) {
        $fileLabel.text('선택된 파일 없음');
    } else {
        $fileLabel.text(`선택된 파일: ${fileInput.files[0].name}`);
    }
  }