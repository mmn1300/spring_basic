document.addEventListener('DOMContentLoaded', () => {
    document.querySelector('.home-button').addEventListener('click', () => {
        window.location.href = '/';
    });

    document.querySelector('.prev-page-button').addEventListener('click', () => {
        window.location.href = '/board';
    });
});