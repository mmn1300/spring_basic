document.addEventListener('DOMContentLoaded', () => {
    setTimeout(getCurrentTime(), 60000);
    checkLogin().then(res => {
        if(res["message"]){
            userInfo = document.querySelector('#user-info');
            userInfo.textContent = `${res['nickname']} 님 (${res['id'].slice(0,4)}****)`;
        }
    });
});