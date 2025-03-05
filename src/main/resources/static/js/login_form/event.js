document.addEventListener('DOMContentLoaded', () => {
    // 로그인 버튼 누를시 요청
    document.querySelector('#login').addEventListener('click', () => {
        login();
    });

    // 엔터키 누를시 요청
    document.addEventListener('keydown', (event) => {
        if (event.key === 'Enter') {
            login();
        }
    });

    // 홈 버튼 누를시 홈으로 이동
    document.querySelector('#home').addEventListener('click', () => {
        window.location.href = '/';
    });

    // ← 버튼 누를시 홈으로 이동
    document.querySelector('#backPage').addEventListener('click', () => {
        window.location.href = '/';
    });

    // Tap 키 누를시 입력칸 이동
    const inputs = document.querySelectorAll('input');
    inputs.forEach((input, index) => {
        input.addEventListener('keydown', function(event) {
            if (event.key === 'Tab') {
                event.preventDefault();
                const nextIndex = (index + 1) % inputs.length;
                inputs[nextIndex].focus();
            }
        });
    });
});