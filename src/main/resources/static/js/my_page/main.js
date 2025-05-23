$(document).ready(() => {
    getCount(userId).then(result => {
        const data = result["data"];
        if(data["message"]){
            $("#my-posts-count").text(data["num"]);
        }
    });
});