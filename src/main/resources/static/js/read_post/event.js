$(document).ready(() => {
    $('.home-button').first().on('click', () => {
        $(location).attr('href', '/');
    });

    $('.prev-page-button').first().on('click', () => {
        $(location).attr('href', '/board');
    });
});