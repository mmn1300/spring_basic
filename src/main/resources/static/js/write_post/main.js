$(document).ready( () => {

    setTimeout(getCurrentTime(), 60000);
    checkLogin().then(res => {
        if(res["message"]){
            userInfo = $('#user-info');
            userInfo.text(`${res['nickname']} 님 (${res['id'].slice(0,4)}****)`);
        }
    });
});