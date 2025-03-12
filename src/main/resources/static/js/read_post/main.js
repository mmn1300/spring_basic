document.addEventListener('DOMContentLoaded', () => {
    const dtDiv = document.querySelector('.date-time');
    dtDiv.textContent = dtDiv.textContent.slice(0,10)+' '+dtDiv.textContent.slice(11);

    checkPostUser(pathVariable).then(response => {
        if(response["boolData"]){
            const postActions = document.querySelector('.post-actions');
            postActions.appendChild(createUDButton(pathVariable));
        }
        // else{
        //     postActions.appendChild(createRecommendButton());
        // }
    });

    // const number = parseInt(document.querySelector('.main-content').id);
    // fileExists(number).then(fileName => {

    //     // 첨부된 파일이 존재할때만 파일 다운로드
    //     if(fileName !==''){
    //         downloadFile(fileName, number);
    //     }
    // });
});