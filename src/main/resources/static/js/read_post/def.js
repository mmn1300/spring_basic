const pathSegments = window.location.pathname.split('/');
const pathVariable = parseInt(pathSegments[pathSegments.length - 1]);

// <button id="recommend">추천하기</button>
const createRecommendButton = () => {
    const button = document.createElement('button');
    button.id = 'recommend';
    button.textContent = '추천하기';

    return button;
};

// <div class="my-post">
//   <button class="post-ud" id="post-update">수정</button>
//   <button class="post-ud" id="post-delete">삭제</button>
// </div>
const createUDButton = (pathVariable) => {
    const div = $('<div class="my-post"></div>');

    const updateButton = $('<button class="post-ud" id="post-update">수정</button>');
    div.append(updateButton);

    const deleteButton = $('<button class="post-ud" id="post-delete">삭제</button>');
    div.append(deleteButton);

    updateButton.on('click', () => {
        $(location).attr('href', `/board/edit/${pathVariable}`);
    });

    deleteButton.on('click', () => {
        if(confirm('이 게시물을 삭제하시겠습니까?')){
            deletePost(pathVariable);
        }
    });

    return div;
};

async function checkPostUser(postNum) {
    return $.ajax({
        url: `/board/user/${postNum}`,
        method: 'GET',
        contentType: 'application/json',
        dataType: 'json',
        success: function(data) {
            if(data['message']){
                return data;
            }else{
                console.error(data['error']);
            }
        },
        error: function(xhr, status, error) {
            alert(`요청 중 에러가 발생했습니다.\n\n${status}, ${error}`);
        }
    });
}



// 비동기 요청을 통해 게시글 삭제를 요청하는 함수
const deletePost = (pathVariable) => {
    $.ajax({
        url: `/board/remove/${pathVariable}`,
        method: 'DELETE',
        contentType: 'application/json',
        dataType: 'json',
        success: function(data) {
            if(data['message']){
                alert('게시글 삭제를 완료하였습니다.');
                $(location).attr('href', '/board');
            }else{
                console.error(data['error']);
            }
        },
        error: function(xhr, status, error) {
            alert(`요청 중 에러가 발생했습니다.\n\n${status}, ${error}`);
        }
    });
};

// 첨부된 파일이 있는지 확인
async function fileExists(number){
    return $.ajax({
        url: `/board/file/${number}`,
        method: 'GET',
        dataType: 'json',
        success: function(data) {
            if(data['message']){
                return data;
            }else{
                console.error(data['error']);
            }
        },
        error: function(xhr, status, error) {
            alert(`요청 중 에러가 발생했습니다.\n\n${status}, ${error}`);
        }
    });
}

// 파일 다운로드
function downloadFile(fileName, number) {
    $.ajax({
        url: `/board/download/${number}`,
        method: 'GET',
        xhrFields: {
            responseType: 'blob' // 서버에서 바이너리 데이터를 받을 수 있도록 설정
        },
        success: function (data) {
            const url = window.URL.createObjectURL(data);
            const a = $(`<a href="${url}" class="file_download" download="${fileName}">${fileName}</a>`);

            // 파일 컨테이너에 a 태그 추가
            $('.file-container').first().append(a);

        },
        error: function (xhr, textStatus, errorThrown) {
            console.error('파일 다운로드 오류:', textStatus, errorThrown);
        }
    });
};