$(document).ready( () => {
    $('#submit-btn').on('click', () => {
        if (confirm('게시물을 수정하시겠습니까?')) {
            updatePost(pathVariable);
        }
    });

    $('.home-button').first().on('click', () => {
        $(location).attr('href', '/');
    });

    $('.prev-page-button').first().on('click', () => {
        // 추후 입력 게시물 존재시 확인하는 기능 추가할 예정
        $(location).attr('href', '/board');
    });

    $('#file-upload').on('change', changeText);
});