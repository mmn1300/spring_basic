$(document).ready(() => {
    getCount(userId).then(result => {
        if(result["message"]){
            $("#my-posts-count").text(result["num"]);
        }
    });
});