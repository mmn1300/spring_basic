const pathSegments = window.location.pathname.split('/');
const pathVariable = parseInt(pathSegments[pathSegments.length - 1]);

// <button id="like">좋아요</button>
const createLikeButton = () => {
    return $('<button id="like">좋아요</button>');
};

// <div class="my-post">
//   <button class="post-ud" id="post-update">수정</button>
//   <button class="post-ud" id="post-delete">삭제</button>
// </div>
const createUDButton = (pathVariable) => {
    const $div = $('<div class="my-post"></div>');

    const $updateButton = $('<button class="post-ud" id="post-update">수정</button>');
    $div.append($updateButton);

    const $deleteButton = $('<button class="post-ud" id="post-delete">삭제</button>');
    $div.append($deleteButton);

    $updateButton.on('click', () => {
        $(location).attr('href', `/board/edit/${pathVariable}`);
    });

    $deleteButton.on('click', () => {
        if(confirm('이 게시물을 삭제하시겠습니까?')){
            deletePost(pathVariable);
        }
    });

    return $div;
};

async function checkPostUser(postNum) {
    return $.ajax({
        url: `/board/user/${postNum}`,
        method: 'GET',
        contentType: 'application/json',
        dataType: 'json',
        success: function(data) {
            const responseData = data["data"];
            if(responseData['message']){
                return data;
            }else{
                console.error(responseData['error']);
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



// 비동기 요청을 통해 게시글 삭제를 요청하는 함수
const deletePost = (pathVariable) => {
    const csrfToken = $("meta[name='_csrf']").attr("content");

    $.ajax({
        url: `/board/post/${pathVariable}`,
        method: 'DELETE',
        headers: {
            "X-XSRF-TOKEN": csrfToken
        },
        contentType: 'application/json',
        dataType: 'json',
        success: function(data) {
            const responseData = data["data"];
            if(responseData['message']){
                alert('게시글 삭제를 완료하였습니다.');
                $(location).attr('href', '/board');
            }else{
                console.error(responseData['error']);
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
};

// 첨부된 파일이 있는지 확인
async function fileExists(number){
    return $.ajax({
        url: `/board/file/${number}`,
        method: 'GET',
        dataType: 'json',
        success: function(data) {
            const responseData = data["data"];
            if(responseData['message']){
                return data;
            }else{
                console.error(responseData['error']);
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

// 파일 다운로드
function downloadFile(fileName, number) {
    $.ajax({
        url: `/board/download/${number}`,
        method: 'GET',
        xhrFields: {
            responseType: 'blob' // 서버에서 바이너리 데이터를 받을 수 있도록 설정
        },
        success: function (data) {
            const $url = window.URL.createObjectURL(data);
            const $a = $(`<a href="${$url}" class="file_download" download="${fileName}">${fileName}</a>`);

            // 파일 컨테이너에 a 태그 추가
            $('.file-container').first().append($a);

        },
        error: function (xhr, textStatus, errorThrown) {
            console.error('파일 다운로드 오류:', textStatus, errorThrown);
        }
    });
};