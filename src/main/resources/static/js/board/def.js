// 한 페이지 당 최대 게시물 개수
const maxRow = 16;

// 현재 페이지 넘버
let pageNum = 1;


// <tr class="table-row">
//     <td class="post-number"></td>
//     <td class="post-title"></td>
//     <td class="post-user"></td>
//     <td class="post-date"></td>
// </tr>
const createTableRow = () => {
    return $(
    '<tr class="table-row">' +
        '<td class="post-number"></td>' +
        '<td class="post-title"></td>' +
        '<td class="post-user"></td>' +
        '<td class="post-date"></td>' +
    '</tr>'
    );
};


const createSearchOptionElements = (searchOption) => {
    const $div = $('<div>' +
             `<span id="text-search">${searchOption}</span>` +
             '</div>');

    const $button = $('<button id="search-user-delete">X</button>');
    $button.on('click', () => { $(location).attr('href', '/board'); });
    $div.append($button);
    return $div;
}


const addSearchOption = (searchOption) => {
    const $td = $("#search-space");
    $td.append(createSearchOptionElements(searchOption));
}


// 로그인이 되어있는지 확인. 요청을 통해 로그인 되어있다면 아이디와 닉네임을 불러옴
async function checkLogin() {
    return $.ajax({
        url: '/session',
        method: 'GET',
        dataType: 'json',
        success: function(data) {
            if (data['error'] !== undefined) {
                console.log(data['error']);
            }
            return data;
        },
        error: function(xhr, status, error) {
            alert(`요청 중 에러가 발생했습니다.\n\n${status}, ${error}`);
        }
    });
};


// 게시글을 게시판에 게시하는 함수
const setPosts = (posts) => {
    clearPost();
    const $tableRowArray = $('.table-row');

    let cnt = 0;
    posts.forEach(row => {
        $tableRowArray.eq(cnt).children('.post-number').text(row["id"]);
        const span = $(`<a href="/board/show/${row["id"]}" class="title-hypertext">${row["title"]}</a>`);
        $tableRowArray.eq(cnt).children('.post-title').append(span);
        $tableRowArray.eq(cnt).children('.post-user').text(`${row["nickname"]} (${row["userId"].slice(0,4)}****)`);
        $tableRowArray.eq(cnt).children('.post-date').text(row["createAt"]);
        cnt++;
    });
};


// 게시글을 요청을 통해 받아오는 함수
const contentLoad = (pageNum) => {
    const param = {page : pageNum};
    $.ajax({
        url: '/board/posts',
        method: 'GET',
        contentType: 'application/json',
        data: param,
        success: function(data) {
            const responseData = data["data"];
            if(responseData['message']){
                if(responseData["rows"] > 0 && responseData["rows"] <= 16){
                    setPosts(responseData["posts"]);
                }else if(responseData["rows"] === 0){
                    clearPost();
                }
            }else{
                console.error('요청 중 오류가 발생했습니다.', data["error"]);
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


// 작성자 별 게시글을 요청을 통해 받아오는 함수
const contentLoadByUser = (pageNum, userId) => {
    const param = {page : pageNum, user: userId};
    $.ajax({
        url: '/board/posts',
        method: 'GET',
        contentType: 'application/json',
        data: param,
        success: function(data) {
            const responseData = data["data"];
            if(responseData['message']){
                if(responseData["rows"] > 0 && responseData["rows"] <= 16){
                    setPosts(responseData["posts"]);
                }else if(responseData["rows"] === 0){
                    clearPost();
                }
            }else{
                console.error('요청 중 오류가 발생했습니다.', data["error"]);
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


// 모든 게시물을 제거하는 함수.
const clearPost = () => {
    const $rows = $('.table-row');
    $rows.each((idx, row) => {
        $(row).find('.post-number').first().text('');
        $(row).find('.post-title').first().empty();
        $(row).find('.post-user').first().text('');
        $(row).find('.post-date').first().text('');
    });
};