// 로그인이 되어있는지 확인. 요청을 통해 로그인 되어있다면 아이디와 닉네임을 불러옴
async function checkLogin() {
    return $.ajax({
        url: '/session',
        method: 'GET',
        dataType: 'json',
        success: function(data) {
            if (data['error'] !== undefined) {
                console.log(data['error']);
            }
            return data;
        },
        error: function(xhr, status, error) {
            alert(`요청 중 에러가 발생했습니다.\n\n${status}, ${error}`);
        }
    });
}

const logout = () =>{
    $.ajax({
        url: '/session/logout',
        method: 'DELETE',
        contentType: 'application/json',
        dataType: 'json',
        success: function(data) {
            if(data["message"]){
                location.reload();
            }else{
                console.error(data['error']);
            }
        },
        error: function(xhr, status, error) {
            alert(`요청 중 에러가 발생했습니다.\n\n${status}, ${error}`);
        }
    });
};