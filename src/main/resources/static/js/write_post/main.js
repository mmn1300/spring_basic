$(document).ready( () => {

    setTimeout(getCurrentTime(), 60000);
    checkLogin().then(res => {
        const responseData = res["data"];
        if(responseData["message"]){
            userInfo = $('#user-info');
            userInfo.text(`${responseData['nickname']} 님 (${responseData['id'].slice(0,4)}****)`);
        }
    });
});