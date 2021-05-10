"use strict";

var hasMore = 0;

function add_posts(){
    $('#load-more-btn').hide();
    $('#loading-more-btn').show();

    var api_end_point = "https://www.socialmediawall.io/api/v1.1/";
    var wall_id = "12077";
    var app_id = "f0afca337586413cae1e68689d5f50b5";
    var app_secret = "abb10a6046d145b0be5e1d417a7f686b";

    var url = api_end_point +
        wall_id +
        "/posts/?" +
        "app_id=" + app_id +
        "&app_secret=" + app_secret +
        "&offset=" + offset +
        "&limit=" + limit +
        "&include_only_image_posts=true";

    console.log(url);

    $.get(
        url,
        function(data) {
            var obj = JSON.stringify(data);
            var obj = JSON.parse(obj);

            hasMore = obj["data"]["hasMore"];

            get_valid_image_posts(obj["data"]["posts"], show_posts, obj);

        }
    );
}


    // "status": "success",
    // "info": [],
    // "current_time": 1505470103,
    // "data": {
    // "image": "https://url.of.some/other/image",
    //     "text": "Picture of a cat",
    //     "user_image": "https://url.of.some/image",
    //     "user_name": "Cat Facts",
    //     "posted_at": "2017-09-15 10:08:23",
    //     "posted_timestamp": 1505470103
