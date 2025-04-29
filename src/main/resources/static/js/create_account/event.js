$(document).ready(() => {
    // 아이디 변경시 플래그 제어
    $('#id').on('change', () => {
        checkFalg["id"] = false;
        const $iconImg = $('.icon-img');
        $iconImg.eq(0).attr('src', uncheckedImgPath);
    });


    // 비밀번호 변경시 플래그 및 메세지 변경
    $('#pw').on('change', (e) => {
        const $pwCheck = $('#pw-check');
        const $pwCheckText = $('#pw-check-text');
        const $iconImg = $('.icon-img');
        checkFalg["pw"] = false;
        $iconImg.eq(1).attr('src', uncheckedImgPath);
        $iconImg.eq(2).attr('src', uncheckedImgPath);
        
        if($(e.currentTarget).val().length >= 8){
            // 글자제한 모두 만족시
            if($(e.currentTarget).val().length <= 15){

                // 비밀번호 확인 미 입력시
                if($pwCheck.val().trim() === ''){
                    $pwCheckText.text('비밀번호 확인란을 입력해주세요.');
                    $pwCheckText.attr('class', 'highlight-black');
                    $iconImg.eq(1).attr('src', checkedImgPath);
                }
                
                // 비밀번호와 확인의 값 일치시
                else if($(e.currentTarget).val() === $pwCheck.val().trim()){
                    $pwCheckText.text('비밀번호가 일치합니다.');
                    $pwCheckText.attr('class', 'highlight-blue');
                    checkFalg["pw"] = true;
                    $iconImg.eq(1).attr('src', checkedImgPath);
                    $iconImg.eq(2).attr('src', checkedImgPath);
                }
                
                // 비밀번호와 확인의 값 미일치시
                else{
                    $pwCheckText.text('비밀번호가 일치하지 않습니다.');
                    $pwCheckText.attr('class', 'highlight-red');
                    $iconImg.eq(1).attr('src', checkedImgPath);
                }
            }else{
                $pwCheckText.text('비밀번호는 열다섯 글자까지만 입력할 수 있습니다.');
                $pwCheckText.attr('class', 'highlight-red');
            }
        }else{
            $pwCheckText.text('비밀번호는 여덟글자 이상이어야 합니다.');
            $pwCheckText.attr('class', 'highlight-red');
        }
    });


    // 비밀번호 확인란 변경시 플래그 및 메세지 변경
    $('#pw-check').on('change', (e) => {
        const $pw = $('#pw');
        const $pwCheckText = $('#pw-check-text');
        const $iconImg = $('.icon-img');
        checkFalg["pw"] = false;
        $iconImg.eq(2).attr('src', uncheckedImgPath);
        
        // 비밀번호 확인 미 입력시
        if($pw.val().trim() === ''){
            $pwCheckText.text('비밀번호란을 입력해주세요.');
            $pwCheckText.attr('class', 'highlight-black');
        }
        
        // 비밀번호와 확인의 값 일치시
        else if($(e.currentTarget).val() === $pw.val().trim()){
            if($pw.val().length >= 8){
                if($pw.val().length <= 15){
                    $pwCheckText.text('비밀번호가 일치합니다.');
                    $pwCheckText.attr('class', 'highlight-blue');
                    checkFalg["pw"] = true;
                    $iconImg.eq(1).attr('src', checkedImgPath);
                    $iconImg.eq(2).attr('src', checkedImgPath);
                }else{
                    $pwCheckText.text('비밀번호는 열다섯 글자까지만 입력할 수 있습니다.');
                    $pwCheckText.attr('class', 'highlight-red');
                    attr('class', 'highlight-red');
                }
            }else{
                $pwCheckText.text('비밀번호는 여덟글자 이상이어야 합니다.');
                $pwCheckText.attr('class', 'highlight-red');
            }

        // 비밀번호와 확인의 값 미일치시
        }else{
            $pwCheckText.text('비밀번호가 일치하지 않습니다.');
            $pwCheckText.attr('class', 'highlight-red');
        }
    });


    // 닉네임 변경시 플래그 제어
    $('#nickname').on('change', (e) => {
        const len = $(e.currentTarget).val().length;
        const $iconImg = $('.icon-img');
        if($(e.currentTarget).val() === '' || len > 10){
            $iconImg.eq(3).attr('src', uncheckedImgPath);
            checkFalg["name"] = false;
        }else{
            $iconImg.eq(3).attr('src', checkedImgPath);
            checkFalg["name"] = true;
        }
    });

    // 이메일 변경시 플래그 제어
    $('#email').on('change', (e) => {
        const emailId = $(e.currentTarget).val();
        const emailDomain = $('#email-domain').val();
        const $iconImg = $('.icon-img');

        if(emailId === '' || emailDomain === ''){
            $iconImg.eq(4).attr('src', uncheckedImgPath);
            checkFalg["email"] = false;
        }else{
            $iconImg.eq(4).attr('src', checkedImgPath);
            checkFalg["email"] = true;
        }
    });

    // 이메일 도메인 주소 변경시 플래그 제어
    $('#email-domain').on('change', (e) => {
        const emailId = $('#email').val();
        const emailDomain = $(e.currentTarget).val();
        const $iconImg = $('.icon-img');

        if(emailId === '' || emailDomain === ''){
            $iconImg.eq(4).attr('src', uncheckedImgPath);
            checkFalg["email"] = false;
        }else{
            $iconImg.eq(4).attr('src', checkedImgPath);
            checkFalg["email"] = true;
        }
    });

    // 전화번호(좌) 변경시 플래그 제어
    $('#phone').on('change', (e) => {
        const phone = $(e.currentTarget).val();
        const phone2 = $('#phone2').val();
        const phone3 = $('#phone3').val();
        const $iconImg = $('.icon-img');
        
        if(phone.length !== 3 || phone2.length !== 4 || phone3.length !== 4){
            $iconImg.eq(5).attr('src', uncheckedImgPath);
            checkFalg["phone"] = false;
        }else{
            $iconImg.eq(5).attr('src', checkedImgPath);
            checkFalg["phone"] = true;
        }
    });

    // 전화번호(중) 변경시 플래그 제어
    $('#phone2').on('change', (e) => {
        const phone = $('#phone').val();
        const phone2 = $(e.currentTarget).val();
        const phone3 = $('#phone3').val();
        const $iconImg = $('.icon-img');
        
        if(phone.length !== 3 || phone2.length !== 4 || phone3.length !== 4){
            $iconImg.eq(5).attr('src', uncheckedImgPath);
            checkFalg["phone"] = false;
        }else{
            $iconImg.eq(5).attr('src', checkedImgPath);
            checkFalg["phone"] = true;
        }
    });

    // 전화번호(우) 변경시 플래그 제어
    $('#phone3').on('change', (e) => {
        const phone = $('#phone').val();
        const phone2 = $('#phone2').val();
        const phone3 = $(e.currentTarget).val();
        const $iconImg = $('.icon-img');
        
        if(phone.length !== 3 || phone2.length !== 4 || phone3.length !== 4){
            $iconImg.eq(5).attr('src', uncheckedImgPath);
            checkFalg["phone"] = false;
        }else{
            $iconImg.eq(5).attr('src', checkedImgPath);
            checkFalg["phone"] = true;
        }
    });


    $('#id-redundancy-check').on('click', () => {
        const id = $("#id").val();
        if(id.length > 15){
            alert('아이디는 열다섯 글자까지만 입력할 수 있습니다.');
        }else if(id.trim() === ''){
            alert('아이디를 입력해주세요.');
        }else{
            idCheckRequest(id).then(result => {
                if(result["message"] === false){
                    alert('생성 가능한 아이디입니다!');
                    const $iconImg = $('.icon-img');
                    $iconImg.eq(0).attr('src', checkedImgPath);
                    checkFalg['id'] = true;
                }else{
                    alert('이미 존재하는 아이디입니다!');
                }
            });
        }
    });


    // Tab 키 누를시 입력창 전환
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


    // 최종 검사 및 계정 생성 요청
    $('#sign-up').on('click', () => {
        createAccount();
    });

    // 이전 페이지로 돌아가기 전 입력창 내부에 값이 남아있을 경우 한번 더 확인
    $('#back-page').on('click', () => {
        const $inputs = $('input');
        let allEmpty = true;

        $inputs.each((index, input) => {
            if ($(input).val().trim() !== '') {
                allEmpty = false;
            }
        });

        if(allEmpty){
            $(location).attr('href', '/login');
        }else if(confirm('회원가입을 취소하시겠습니까?')){
            $(location).attr('href', '/login');
        }
    });
});