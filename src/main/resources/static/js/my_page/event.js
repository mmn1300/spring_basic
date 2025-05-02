$(document).ready(() => {

    // 홈 버튼 누를시 홈으로 이동
    $('#home').on('click', () => {
        $(location).attr('href', '/');
    });

    // ← 버튼 누를시 홈으로 이동
    $('#backPage').on('click', () => {
        $(location).attr('href', window.history.go(-1));
    });

    $('.change-btn').on('click', (e) => {
        const $tr = $(e.currentTarget).closest('tr');
        const $span = $tr.find("td .table-div span");
        if($span.length > 0){
            changeSpanToInput($tr, $span, $(e.currentTarget));
        }else{
            const $input = $tr.find("td .table-div-no-bold input");
            changeInputToSpan($tr, $input, $(e.currentTarget));
        }
        ToggleUpdateElements();
    });

    $('#my-posts-query').on('click', () => {
        $(location).attr('href', `/board?user=${userId}`);
    });
    
});