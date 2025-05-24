// 로그인이 되어있는지 확인. 요청을 통해 로그인 되어있다면 아이디와 닉네임을 불러옴
async function checkLogin() {
    return $.ajax({
        url: '/session',
        method: 'GET',
        success: function(data) {
            return data;
        },
        error: function (xhr, textStatus, errorThrown) {
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
};

// 글을 작성하는 함수.
function writePostEvent() {
    const title = $('#title-input').val();
    const content = $('#content').val();
    
    if(title.trim() !== ''){
        if(content.trim() !== ''){
            const files = $('#file-upload')[0];
            const file = (files.files[0] === undefined ? null : files.files[0]);
            writePost(title, content, file);
        }else{
            alert('내용을 입력해주세요.');
        }
    }else{
        alert('제목을 입력해주세요.');
    }
};

function writePost(title, content, file){
    const formData = new FormData();
    formData.append('title', title);
    formData.append('content', content);
    formData.append('file', file);

    $.ajax({
        url: '/board/post',
        method: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function(data) {
            const responseData = data["data"];
            if(responseData["message"]){
                alert("게시글이 성공적으로 작성되었습니다.");
                window.location.href = '/board';
            }else{
                alert("게시글 작성을 실패하였습니다.");
                console.error(responseData["error"]);
            }
        },
        error: function (xhr, textStatus, errorThrown) {
            console.error('게시글 작성 오류:', textStatus, errorThrown);
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
    })
};

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