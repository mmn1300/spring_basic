document.addEventListener('DOMContentLoaded', () => {
    document.querySelector('#submit-btn').addEventListener('click', () => {
        if(confirm('게시물을 수정하시겠습니까?')){
            updatePost(pathVariable);
        }
    });

    document.querySelector('.home-button').addEventListener('click', () => {
        window.location.href = '/';
    });

    document.querySelector('.prev-page-button').addEventListener('click', () => {
        // 추후 입력 게시물 존재시 확인하는 기능 추가할 예정
        window.location.href = '/board';
    });

    document.querySelector('#file-upload').addEventListener('change', changeText);
});