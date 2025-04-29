$(document).ready(() => {

    // 로그인 버튼 누를시 요청
    $('#login').on('click', () => {
        login();
    });

    // 엔터키 누를시 요청
    $('keydown', (event) => {
        if (event.key === 'Enter') {
            login();
        }
    });

    // 홈 버튼 누를시 홈으로 이동
    $('#home').on('click', () => {
        $(location).attr('href', '/');
    });

    // ← 버튼 누를시 홈으로 이동
    $('#backPage').on('click', () => {
        $(location).attr('href', '/');
    });

    // Tap 키 누를시 입력칸 이동
    const $inputs = $('input');
    $inputs.each((index, input) => {
        $(input).on('keydown', (event) => {
            if (event.key === 'Tab') {
                event.preventDefault();
                const nextIndex = (index + 1) % $inputs.length;
                $inputs[nextIndex].focus();
            }
        });
    });
});