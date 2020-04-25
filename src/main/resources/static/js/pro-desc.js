window.onload = function() {
    var oDiv = document.getElementById("div1");
    var oMark = document.getElementsByClassName("mark")[0];
    var oFloat = document.getElementsByClassName("float_layer")[0];
    var oBig = document.getElementsByClassName("big_pic")[0];
    var oSmall = document.getElementsByClassName("small_pic")[0];
    var oImg = oBig.getElementsByTagName("img")[0];
    //给遮罩层添加鼠标移入事件
    oMark.onmouseover = function() {
        oFloat.style.display = 'block';
        oBig.style.display = 'block';
    };
    //给遮罩层添加鼠标移出事件
    oMark.onmouseout = function() {
        oFloat.style.display = "none";
        oBig.style.display = "none";
    };
    //给遮罩层添加鼠标移动事件
    oMark.onmousemove = function(evt) {
        var e = evt || window.event;
        document.title = e.clientX - oDiv.offsetLeft - oSmall.offsetLeft;
        var l = e.clientX - oDiv.offsetLeft - oSmall.offsetLeft - oFloat.offsetWidth / 2;
        var t = e.clientY - oDiv.offsetTop - oSmall.offsetTop - oFloat.offsetHeight / 2;
        if (l < 0) {
            l = 0;
        } else if (l > oMark.offsetWidth - oFloat.offsetWidth) {
            l = oMark.offsetWidth - oFloat.offsetWidth;
        }
        if (t < 0) {
            t = 0;
        } else if (t > oMark.offsetHeight - oFloat.offsetHeight) {
            t = oMark.offsetHeight - oFloat.offsetHeight;
        }
        oFloat.style.left = l + 'px';
        oFloat.style.top = t + 'px';
        //小块活动的距离 （移动比例）大图显示比例
        var percentX = l / (oMark.offsetWidth - oFloat.offsetWidth);
        var percentY = t / (oMark.offsetHeight - oFloat.offsetHeight);
        //大图的left值 = 移动比例 * （大图的宽度 - 大图所在Div的宽度）大图所能移动的距离
        oImg.style.left = -percentX * (oImg.offsetWidth - oBig.offsetWidth) + 'px';
        oImg.style.top = -percentY * (oImg.offsetHeight - oBig.offsetHeight) + 'px';
    };
};

//加入购物车
$(document).ready(function(){
    $("#buyNowBtn").on("click", function(){
        var productId = $("#productId").val();

        var order = new Array();
        order.push({
            "id": productId,
            "amount": 1
        });
        $.ajax({
            type: "post",
            url: "generateOrder",
            data: JSON.stringify(order),
            dataType: "json",
            contentType : "application/json",
            success: function(data) {
                if (data.result == "Success") {
                    alert("商品已下单，可在定单中查看！");
                }
                else if(data.result == "NotLogin") {
                    alert("请先登录");
                    location.href = "/user/login";
                }
                else{
                    alert("下单失败，" + data.result);
                }
            },
            error: function(err) {
                alert(err);
            }
        });
    });

    $("#addToCartBtn").on("click", function(){
        var productId = $("#productId").val();

        var formData = new FormData();
        formData.append("productId", productId);
        formData.append("amount", "1");
        $.ajax({
            type: "post",
            url: "addToCart",
            data: formData,
            processData : false,
            contentType : false,
            success: function(data) {
                if (data.result == "Success") {
                    alert("商品已添加到购物车，可在购物车中查看！");
                }
                else if(data.result == "NotLogin") {
                    alert("请先登录");
                    location.href = "/user/login";
                }
                else{
                    alert("添加到购物车失败，" + data.result);
                }
            },
            error: function(err) {
                alert(err);
            }
        });
    });
});
