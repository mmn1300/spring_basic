document.addEventListener('DOMContentLoaded', () => {
    document.querySelector('#home-button').addEventListener('click', () => {
        window.location.href = '/';
    });

    document.querySelector('#write-post').addEventListener('click', () => {
        window.location.href = '/post/write';
    });

    document.querySelector('#prev-page').addEventListener('click', () => {
        const pageNum = parseInt(document.querySelector('#page-number').textContent);
        console.log(`${pageNum}`);
        if(pageNum > 1){
            contentLoad(pageNum-1);
            document.querySelector('#page-number').textContent = String(pageNum-1);
        }else{
            alert('첫번째 페이지입니다.');
        }
    });

    document.querySelector('#next-page').addEventListener('click', () => {
        const pageNum = parseInt(document.querySelector('#page-number').textContent);
        const posts = document.querySelectorAll('.title-hypertext');
        if(posts.length === maxRow){
            contentLoad(pageNum+1);
            document.querySelector('#page-number').textContent = String(pageNum+1);
            console.log(`${parseInt(document.querySelector('#page-number').textContent)}     ${document.querySelectorAll('.title-hypertext').length}`);
        }else{
            alert('마지막 페이지입니다.');
        }
    });
});