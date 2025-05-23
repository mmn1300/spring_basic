$(document).ready(() => {

    checkPostUser(pathVariable).then(response => {
        if(response["data"]){
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