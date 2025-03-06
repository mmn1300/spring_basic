document.addEventListener('DOMContentLoaded', () => {
    // 아이디 변경시 플래그 제어
    document.querySelector('#id').addEventListener('change', () => {
        checkFalg["id"] = false;
        const iconImg = document.querySelectorAll('.icon-img');
        iconImg[0].src = uncheckedImgPath;
    });

    // 비밀번호 변경시 플래그 및 메세지 변경
    document.querySelector('#pw').addEventListener('change', (event) => {
        const pwCheck = document.querySelector('#pw-check');
        const pwCheckText = document.querySelector('#pw-check-text');
        const iconImg = document.querySelectorAll('.icon-img');
        checkFalg["pw"] = false;
        iconImg[1].src = uncheckedImgPath;
        iconImg[2].src = uncheckedImgPath;
        

        if(event.target.value.length >= 8){
            if(event.target.value.length <= 15){
                if(pwCheck.value.trim() === ''){
                    pwCheckText.textContent = '비밀번호 확인란을 입력해주세요.';
                    pwCheckText.className = 'highlight-black';
                    iconImg[1].src = checkedImgPath;
                }else if(event.target.value === pwCheck.value.trim()){
                    pwCheckText.textContent = '비밀번호가 일치합니다.';
                    pwCheckText.className = 'highlight-blue';
                    checkFalg["pw"] = true;
                    iconImg[1].src = checkedImgPath;
                    iconImg[2].src = checkedImgPath;
                }else{
                    pwCheckText.textContent = '비밀번호가 일치하지 않습니다.';
                    pwCheckText.className = 'highlight-red';
                    iconImg[1].src = checkedImgPath;
                }
            }else{
                pwCheckText.textContent = '비밀번호는 열다섯 글자까지만 입력할 수 있습니다.';
                pwCheckText.className = 'highlight-red';
            }
        }else{
            pwCheckText.textContent = '비밀번호는 여덟글자 이상이어야 합니다.';
            pwCheckText.className = 'highlight-red';
        }
    });

    // 비밀번호 확인란 변경시 플래그 및 메세지 변경
    document.querySelector('#pw-check').addEventListener('change', (event) => {
        const pw = document.querySelector('#pw');
        const pwCheckText = document.querySelector('#pw-check-text');
        const iconImg = document.querySelectorAll('.icon-img');
        checkFalg["pw"] = false;
        iconImg[2].src = uncheckedImgPath;
        
        if(pw.value.trim() === ''){
            pwCheckText.textContent = '비밀번호란을 입력해주세요.';
            pwCheckText.className = 'highlight-black';
        }else if(event.target.value === pw.value.trim()){
            if(pw.value.length >= 8){
                if(pw.value.length <= 15){
                    pwCheckText.textContent = '비밀번호가 일치합니다.';
                    pwCheckText.className = 'highlight-blue';
                    checkFalg["pw"] = true;
                    iconImg[1].src = checkedImgPath;
                    iconImg[2].src = checkedImgPath;
                }else{
                    pwCheckText.textContent = '비밀번호는 열다섯 글자까지만 입력할 수 있습니다.';
                    pwCheckText.className = 'highlight-red';
                }
            }else{
                pwCheckText.textContent = '비밀번호는 여덟글자 이상이어야 합니다.';
                pwCheckText.className = 'highlight-red';
            }
        }else{
            pwCheckText.textContent = '비밀번호가 일치하지 않습니다.';
            pwCheckText.className = 'highlight-red';
        }
    });

    // 닉네임 변경시 플래그 제어
    document.querySelector('#nickname').addEventListener('change', (e) => {
        const len = e.target.value.length;
        const iconImg = document.querySelectorAll('.icon-img');
        if(e.target.value === '' || len>10){
            iconImg[3].src = uncheckedImgPath;
            checkFalg["name"] = false;
        }else{
            iconImg[3].src = checkedImgPath;
            checkFalg["name"] = true;
        }
    });

    // 이메일 변경시 플래그 제어
    document.querySelector('#email').addEventListener('change', (e) => {
        const emailId = e.target.value;
        const emailDomain = document.querySelector('#email-domain').value;
        const iconImg = document.querySelectorAll('.icon-img');

        if(emailId === '' || emailDomain === ''){
            iconImg[4].src = uncheckedImgPath;
            checkFalg["email"] = false;
        }else{
            iconImg[4].src = checkedImgPath;
            checkFalg["email"] = true;
        }
    });

    // 이메일 도메인 주소 변경시 플래그 제어
    document.querySelector('#email-domain').addEventListener('change', (e) => {
        const emailId = document.querySelector('#email').value;
        const emailDomain = e.target.value;
        const iconImg = document.querySelectorAll('.icon-img');

        if(emailId === '' || emailDomain === ''){
            iconImg[4].src = uncheckedImgPath;
            checkFalg["email"] = false;
        }else{
            iconImg[4].src = checkedImgPath;
            checkFalg["email"] = true;
        }
    });

    // 전화번호(좌) 변경시 플래그 제어
    document.querySelector('#phone').addEventListener('change', (e) => {
        const phone = e.target.value;
        const phone2 = document.querySelector('#phone2').value;
        const phone3 = document.querySelector('#phone3').value;
        const iconImg = document.querySelectorAll('.icon-img');
        
        if(phone.length !== 3 || phone2.length !== 4 || phone3.length !== 4){
            iconImg[5].src = uncheckedImgPath;
            checkFalg["phone"] = false;
        }else{
            iconImg[5].src = checkedImgPath;
            checkFalg["phone"] = true;
        }
    });

    // 전화번호(중) 변경시 플래그 제어
    document.querySelector('#phone2').addEventListener('change', (e) => {
        const phone = document.querySelector('#phone').value;
        const phone2 = e.target.value;
        const phone3 = document.querySelector('#phone3').value;
        const iconImg = document.querySelectorAll('.icon-img');
        
        if(phone.length !== 3 || phone2.length !== 4 || phone3.length !== 4){
            iconImg[5].src = uncheckedImgPath;
            checkFalg["phone"] = false;
        }else{
            iconImg[5].src = checkedImgPath;
            checkFalg["phone"] = true;
        }
    });

    // 전화번호(우) 변경시 플래그 제어
    document.querySelector('#phone3').addEventListener('change', (e) => {
        const phone = document.querySelector('#phone').value;
        const phone2 = document.querySelector('#phone2').value;
        const phone3 = e.target.value;
        const iconImg = document.querySelectorAll('.icon-img');
        
        if(phone.length !== 3 || phone2.length !== 4 || phone3.length !== 4){
            iconImg[5].src = uncheckedImgPath;
            checkFalg["phone"] = false;
        }else{
            iconImg[5].src = checkedImgPath;
            checkFalg["phone"] = true;
        }
    });


    document.querySelector('#id-redundancy-check').addEventListener('click', () => {
        const id = document.querySelector('#id');
        idCheck(id.value).then(result => {
            if(!result){ // 해당 id가 존재하지 않을 때
                if(id.value.length > 15){
                    alert('아이디는 열다섯 글자까지만 입력할 수 있습니다.');
                }else if(id.value.trim() === ''){
                    alert('아이디를 입력해주세요.');
                }else{
                    alert('생성 가능한 아이디입니다!');
                    const iconImg = document.querySelectorAll('.icon-img');
                    iconImg[0].src = checkedImgPath;
                    checkFalg['id'] = true;
                }
            }else{
                alert('이미 존재하는 아이디입니다!');
            }
        });
    });


    // Tab 키 누를시 입력창 전환
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


    // 최종 검사 및 계정 생성 요청
    document.querySelector('#sign-up').addEventListener('click', () => {
        createAccount();
    });

    // 이전 페이지로 돌아가기 전 입력창 내부에 값이 남아있을 경우 한번 더 확인
    document.querySelector('#back-page').addEventListener('click', () => {
        const inputs = document.querySelectorAll('input');
        let allEmpty = true;

        inputs.forEach(input => {
            if (input.value.trim() !== '') {
                allEmpty = false;
            }
        });

        if(allEmpty){
            window.location.href = '/login';
        }else if(confirm('회원가입을 취소하시겠습니까?')){
            window.location.href = '/login';
        }
    });
});