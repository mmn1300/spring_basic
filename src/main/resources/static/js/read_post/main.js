$(document).ready(() => {

    checkPostUser(pathVariable).then(response => {
        const data = response["data"];
        if(data["message"] && data["data"]){
            const $postActions = $('.post-actions').first();
            $postActions.append(createUDButton(pathVariable));
        }
        // else if(data["message"] && data["data"] === false){
        //     postActions.append(createLikeButton());
        // }
    });

    fileExists(pathVariable).then(res => {
        // 첨부된 파일이 존재할때만 파일 다운로드
        const fileName = res["data"]['fileName'];
        if(fileName !==''){
            downloadFile(fileName, pathVariable);
        }
    });
});