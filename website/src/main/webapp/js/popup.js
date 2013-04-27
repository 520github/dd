var imgPath = "/zh_CN/images/";

//弹出消息框
function openPopupMessage(type,message) {
    setPopupMessage(type,message);
    autoLayer();
    setTimeout("hiddenPopup()",2000); 
}

function openPopupMessageHand(type,message) {
    setPopupMessage(type,message);
    handLayer();
}

function hiddenPopup() {
    $("#messageId").hide();
}

function setPopupMessage(type,message) {
    var template = jQuery("#messageTemplate");// .pop-up-later
    var pop = jQuery("#messageId #pop-up");

    var src = "";
    if(type == "success") {//操作成功
        src = imgPath +"success-cf.png";
    }
    else if(type == "failure") {//操作失败
        src = imgPath +"failure-cf.png";
    }
    else if(type == "later") {//稍后看
       src = imgPath + "pic-9.png";
       message = "该内容已存入<strong>稍后看！</strong>";
    }
    else if(type == "collect") {//收藏
       src = imgPath + "pic-10.png";
       message = "该内容<strong>设置最爱成功！</strong>";
    }
    else if(type == "uncollect") {//取消收藏
       src = imgPath + "pic-11.png";
       message = "该内容已<strong>取消我的最爱！</strong>";
    }
    else if(type == "cancelFriendShare") {//取消朋友分享
       src = imgPath + "pic-11.png";
       message = "该内容已<strong>成功删除！</strong>";
    }
    else if(type == "guestShare") {//匿名分享
       
    }

    var html = template.html();
    html = html.replace("%message%",message);

    pop.empty();
    pop.html(html);
    pop.find("img").attr("src",src);
}

function handLayer() {
    $("#messageId").show();
    $.layer({
        v_istitle:false,
        v_btns : 0,
        v_box : 1,
        v_dom : '#messageId #pop-up',
        v_area : ['450px','30px']
    });
}

function autoLayer() {
    $("#messageId").show();
    $.layer({
        v_istitle:false,
        v_btns : 0,
        v_box : 1,
        v_showclose:false,
        v_dom : '#messageId #pop-up',
        v_showtime : 2,
        v_area : ['450px','30px']
       // v_offset : ['100px','400px']       //为空时数据默认
    });
}

function showLayer(jqueryId) {
    $(jqueryId).show();
    $.layer({
        v_istitle:false,
        v_btns : 0,
        v_box : 1,
        v_showclose:false,
        v_dom : jqueryId,
        v_area : ['450px','30px']
       // v_offset : ['100px','400px']       //为空时数据默认
    });
}

function showFooterLayer(jqueryId) {
    $(jqueryId).show();
    $.layer({
        v_istitle:false,
        v_btns : 0,
        v_box : 1,
        v_showclose:false,
        v_dom : jqueryId,
        v_area : ['1000px','96px'],
        v_offset : ['100px','400px']       //为空时数据默认
    });
}


