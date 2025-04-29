$(document).ready(() => {

    const $dtDiv = $('.date-time').first();
    $dtDiv.text($dtDiv.text().slice(0,10)+' '+$dtDiv.text().slice(11));

    checkPostUser(pathVariable).then(response => {
        if(response["boolData"]){
            const $postActions = $('.post-actions').first();
            $postActions.append(createUDButton(pathVariable));
        }
        // else{
        //     postActions.append(createLikeButton());
        // }
    });

    fileExists(pathVariable).then(res => {
        // 첨부된 파일이 존재할때만 파일 다운로드
        const fileName = res['fileName'];
        if(fileName !==''){
            downloadFile(fileName, pathVariable);
        }
    });
});