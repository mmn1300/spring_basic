// // 로그인이 되어있는지 확인. 요청을 통해 로그인 되어있다면 아이디와 닉네임을 불러옴
// async function checkLogin() {
//     return fetch('/session', {
//         method: 'GET',
//         headers: {
//             'Content-Type': 'application/json'
//         }
//     })
//     .then(response => {
//         if (!response.ok) {
//             throw new Error(`HTTP 오류. 상태코드: ${response.status}`);
//         }
//         return response.json();
//     })
//     .then(data => {
//         if(data['error'] !== undefined){
//             console.log(data['error']);
//         }
//         return data;
//     })
//     .catch((error) => {
//         alert(`요청 중 에러가 발생했습니다.\n\n${error.message}`);
//     });
// }

// const logout = () =>{
//     fetch('/logout', {
//         method: 'DELETE',
//         dataType : 'json',
//         headers: {
//             'Content-Type': 'application/json'
//         }
//     })
//     .then(response => {
//         if (!response.ok) {
//             throw new Error(`HTTP 오류. 상태코드: ${response.status}`);
//         }
//         return response.json();
//     })
//     .then(data => {
//         if(data["message"]){
//             location.reload();
//         }else{
//             console.error(data['error']);
//         }
//     })
//     .catch((error) => {
//         alert(`요청 중 에러가 발생했습니다.\n\n${error.message}`);
//     });
// };