// document.addEventListener('DOMContentLoaded', () => {
//     // 로그인 여부 검사
//     checkLogin().then(response => {
//         if(response["message"]){
//             // 로그인 되어있을 시 로그인 상태 변경
//             const userInfo = document.querySelector('#login-info');
//             userInfo.textContent = `${response['nickname']} 님 (${response['id'].slice(0,4)}****)`;
//             const loginBtn = document.querySelector('#login');
//             loginBtn.textContent = '로그아웃';
//         }
//     });
// });
