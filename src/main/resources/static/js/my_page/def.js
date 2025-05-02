const userId = $("#userid").text();
const nickname = $("#nickname").text();
const email = $("#email").text();
const phone = $("#phone").text();

const accountInfoArray = [userId, nickname, email, phone];


// .account-info-update인 div 아래에 다음 요소들 생성
// <button id="update" class="update-btn">적용하기</button>
// <button id="cancel" class="update-btn">취소하기</button> 
const createUpdateElements = () => {
    if($("#update").length === 0){
        const $div = $(".account-info-update");

        // 계정 정보 갱신 요청 처리
        const $updateButton = $('<button id="update" class="update-btn">적용하기</button>');
        $updateButton.on('click', () => {
            updateAccount(userId).then(result => {
                if(result["message"]){
                    alert("요청이 성공적으로 완료되었습니다.");
                    location.reload();
                }else{
                    alert("요청 중 오류가 발생했습니다.\n\n" + result["error"]);
                }
            });
        });
        $div.append($updateButton);

        // 계정 정보 입력 취소
        const $cancelButton = $('<button id="cancel" class="update-btn">취소하기</button>');
        $cancelButton.on('click', () => {
            const $trs = $('tr[toggled="true"]');
            if($trs.length > 0){
                if(confirm("모든 입력을 취소하시겠습니까?")){
                    $trs.each((idx, tr) => {
                        const $button = $(tr).find("button.change-btn");
                        changeInputToSpan($(tr), $button);
                    });
                    removeUpdateElements();
                }
            }
        });

        $div.append($cancelButton);
    }
};


const removeUpdateElements = () => {
    const $div = $(".account-info-update");
    $div.empty();
};


const ToggleUpdateElements = () => {
    if($('tr[toggled="true"]').length > 0){
        createUpdateElements();
    }else{
        removeUpdateElements();
    }
};


const changeSpanToInput = ($tr, $span, $e) => {
    const $input = $('<input>');
    const index = $("tr").index($tr);

    $input.attr("id", $span.attr('id'));
    $input.val(accountInfoArray[index-1]);

    if(index === 1){
        $input.attr("class", "info-input-user-id");
        $input.attr("check", "false");

        // 입력값 변경시 중복 확인 무효 처리
        $input.on("change", () => {
            $input.attr("check", "false");
            $input.css("background-color", "white");
        });
        // colspan -> 1로 변경
        const $firstTd = $tr.children("td").first();
        $firstTd.attr("colspan", "1");
        $firstTd.children("div").first().attr("id", "userid-div");
        const $secondTd = $('<td></td>');
        
        // 중복확인 버튼 추가
        const $button = $('<button class="change-btn">중복확인</button>');
        $button.on('click', () => { idCheck() });
        $secondTd.append($button);
        $secondTd.insertAfter($firstTd);
    }else{
        $input.attr("class", "info-input");
    }
    $span.replaceWith($input);

    ToggleElement($tr, $input, $e, true);
};


const changeInputToSpan = ($tr, $input, $e) => {
    const $newSpan = $('<span class="info-span"></span>');
    const index = $("tr").index($tr);

    if(index === 1){
        // colspan -> 2로 변경, 중복확인 버튼 제거
        $tr.find("td").eq(1).remove();
        const $firstTd = $tr.find("td").first();
        $firstTd.attr("colspan", "2");
        $firstTd.children("div").first().attr("id", "userid-div-disabled");
    }
    $newSpan.attr("id", $input.attr('id'));
    $newSpan.text(accountInfoArray[index-1]);
    $input.replaceWith($newSpan);

    ToggleElement($tr, $input, $e, false);
};


const ToggleElement = ($tr, $input, $e, flag) => {
    if(flag){
        const $div = $tr.find(".table-div");
        $div.attr("class", "table-div-no-bold");
        $tr.attr("toggled", "true");
        $input.focus()[0].setSelectionRange($input.val().length, $input.val().length);
        $e.text("취소하기");
    }else{
        const $div = $tr.find(".table-div-no-bold");
        $div.attr("class", "table-div");
        $tr.attr("toggled", "false");
        $e.text("변경하기");
    }
};


const idCheck = () => {
    const id = $("#userid").val();
    if(id.length > 15){
        alert('아이디는 열다섯 글자까지만 입력할 수 있습니다.');
    }else if(id.length < 8){
        alert('아이디는 여덟 글자 이상 입력해야 합니다.');
    }else if(id.trim() === ''){
        alert('아이디를 입력해주세요.');
    }else{
        idCheckRequest(id).then(result => {
            // 중복 확인 통과
            if(result["message"] === false){
                alert('생성 가능한 아이디입니다!');
                const $id = $("#userid");
                $id.attr("check", "true");
                $id.css("background-color", "#BBBBBB");
            }else{
                alert('이미 존재하는 아이디입니다!');
            }
        });
    }
};


// 중복 확인 요청
async function idCheckRequest(id){
    return $.ajax({
        url: `/account/${id}`,
        method: 'GET',
        dataType: 'json',
        success: function(data) {
            return data;
        },
        error: function(xhr, status, error) {
            console.error(`요청 중 에러가 발생했습니다.\n\n${status}, ${error}`);
        }
    });
};


// 계정 정보 갱신 요청
async function updateAccount(userId){
    const requestData = {
        userId: $("#userid"),
        nickname: $("#nickname"),
        email: $("#email"),
        phone: $("#phone")
    };
    
    // 요청 데이터 가공
    for (let key in requestData){
        if(requestData[key].is("span")){
            requestData[`${key}`] = requestData[key].text();
        }else if(requestData[key].is("input")){
            // userId 값 중복 확인 체크 여부 확인
            if(key === "userId"){
                if(requestData[key].attr("check") === "false"){
                    alert("아이디 중복 확인이 필요합니다.");
                    return;
                }
            }
            requestData[`${key}`] = requestData[key].val();
        }else{
            alert("입력 형식이 잘못되었습니다.");
            return;
        }
    };

    return $.ajax({
        url: `/account/${userId}`,
        method: 'PUT',
        contentType: 'application/json',
        dataType: 'json',
        data: JSON.stringify(requestData),
        success: function(data) {
            return data;
        },
        error: function(xhr, status, error) {
            alert(`요청 중 에러가 발생했습니다.\n\n${status}, ${error}`);
        }
    });
};

async function getCount(userId) {
    return $.ajax({
        url: `/board/${userId}/posts/count`,
        method: 'GET',
        success: function(data){
            return data;
        },
        error: function(xhr, status, error) {
            alert(`요청 중 에러가 발생했습니다.\n\n${status}, ${error}`);
        }
    })
}