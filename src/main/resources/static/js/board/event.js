$(document).ready(() => {
    $('#home-button').on('click', () => {
        $(location).attr('href', '/');
    });

    $('#write-post').on('click', () => {
        $(location).attr('href', '/board/create');
    });

    $('#prev-page').on('click', () => {
        if(pageNum > 1){
            if (searchOptions.user !== "") {
                contentLoadByUser(--pageNum, searchOptions.user);
            }else{
                contentLoad(--pageNum);
            }
            $('#page-number').text(String(pageNum));
        }else{
            alert('첫번째 페이지입니다.');
        }
    });

    $('#next-page').on('click', () => {
        const $posts = $('.title-hypertext');
        if($posts.length === maxRow){
            if (searchOptions.user !== "") {
                contentLoadByUser(++pageNum, searchOptions.user);
            }else{
                contentLoad(++pageNum);
            }
            $('#page-number').text(String(pageNum));
        }else{
            alert('마지막 페이지입니다.');
        }
    });
});