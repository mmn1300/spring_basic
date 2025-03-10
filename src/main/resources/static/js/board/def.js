// 한 페이지 당 최대 게시물 개수
const maxRow = 16;


// <tr class="table-row">
//     <td class="post-number"></td>
//     <td class="post-title"></td>
//     <td class="post-user"></td>
//     <td class="post-date"></td>
// </tr>
const create_table_row = () => {
    const tr = document.createElement('tr');
    tr.className = 'table-row';

    const tdNumber = document.createElement('td');
    tdNumber.className = 'post-number';
    tr.appendChild(tdNumber);

    const tdTitle = document.createElement('td');
    tdTitle.className = 'post-title';
    tr.appendChild(tdTitle);

    const tdUser = document.createElement('td');
    tdUser.className = 'post-user';
    tr.appendChild(tdUser);

    const tdDelete = document.createElement('td');
    tdDelete.className = 'post-date';
    tr.appendChild(tdDelete);

    return tr;
};

// 로그인이 되어있는지 확인. 요청을 통해 로그인 되어있다면 아이디와 닉네임을 불러옴
async function checkLogin() {
    return fetch('/account/session', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP 오류. 상태코드: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        if(data['error'] !== undefined){
            console.log(data['error']);
        }
        return data;
    })
    .catch((error) => {
        alert(`요청 중 에러가 발생했습니다.\n\n${error.message}`);
    });
};

// 해당 게시글로 이동하는 함수
const loadPost = (postNum) => {
    window.location.href = `/board/show/${postNum}`;
};

// 게시글을 게시판에 게시하는 함수
const setPosts = (posts) => {
    clearPost();
    const tableRowArray = document.querySelectorAll('.table-row');

    let cnt = 0;
    posts.forEach(row => {
        tableRowArray[cnt].querySelector('.post-number').textContent = row["id"];
        const span = document.createElement('span');
        span.className = 'title-hypertext';
        span.textContent = row["title"];
        span.addEventListener('click', () => loadPost(row["id"]));
        tableRowArray[cnt].querySelector('.post-title').appendChild(span);
        tableRowArray[cnt].querySelector('.post-user').textContent = `${row["nickname"]} (${row["userId"].slice(0,4)}****)`;
        tableRowArray[cnt].querySelector('.post-date').textContent = row["createAt"].slice(0,10)+' '+row["createAt"].slice(11);
        cnt++;
    });
};

// 게시글을 요청을 통해 받아오는 함수
const contentLoad = (pageNum) => {
    fetch(`/board/${pageNum}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    }).then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        if(data['message']){
            if(data["rows"] > 0 && data["rows"] <= 16){
                setPosts(data["posts"]);
            }else if(data["rows"] === 0){
                clearPost();
            }
        }else{
            console.error('요청 중 오류가 발생했습니다.', data["error"]);
        }
    })
    .catch(error => {
        console.error('요청 중 오류가 발생했습니다\n\n오류 종류 : ', error);
    });
};

// 모든 게시물을 제거하는 함수.
const clearPost = () => {
    const rows = document.querySelectorAll('.table-row');
    rows.forEach(row => {
        const tdNums = row.querySelectorAll('.post-number');
        const tdTitles = row.querySelectorAll('.post-title');
        const tdUsers = row.querySelectorAll('.post-user');
        const tdDates = row.querySelectorAll('.post-date');
        
        tdNums.forEach(td => {
            td.textContent = '';
        });
        tdTitles.forEach(td => {
            while (td.firstChild) {
                td.removeChild(td.firstChild);
              }
        });
        tdUsers.forEach(td => {
            td.textContent = '';
        });
        tdDates.forEach(td => {
            td.textContent = '';
        });
    });
};