//JQUERY加载完成后
$(document).ready(function(){

  // 导航分类的显示与隐藏
  // 品牌分类
  $("#brand-outer").mouseover(function(){
    $("#nav-category").show();
  });

  $("#brand-outer").mouseout(function(){
    $("#nav-category").hide();
  });

  $(".nav-category-item").mouseover(function(){
    $(this).find(".nav-sub-category").show();
  });

  $(".nav-category-item").mouseout(function(){
    $(this).find(".nav-sub-category").hide();
  });

});

//导航栏商品分类鼠标悬浮
$(document).ready(function(){

  $(".computer-item").mouseover(function(){
    var itemId = this.id;
    var index = itemId.substring(itemId.indexOf('_') + 1);
    var subItemId = 'categorySub_' + index;
    $("#" + subItemId).show();
  });

  $(".computer-item").mouseout(function(){
    var itemId = this.id;
    var index = itemId.substring(itemId.indexOf('_') + 1);
    var subItemId = 'categorySub_' + index;
    $("#" + subItemId).hide();
  });

});

//轮播图
$(function() {
    var index = 0;
    var lis = $(".list").find("li");
    var t;
    t = setInterval(play, 3000);

    function play() {
        index++;
        if (index > $(".lbview li").length - 1) {
            index = 0;
        }
        $(".lbview li").eq(index).fadeIn(1000).siblings().fadeOut();
        lis.eq(index).addClass("zi").siblings().removeClass("zi");
    }
    $(".btn-prev").click(function() {
        index--;
        $(".lbview li").eq(index).fadeIn(1000).siblings().fadeOut();
        lis.eq(index).addClass("zi").siblings().removeClass("zi");
    });

    $(".btn-prev").hover(function() {
        $(".btn-next").animate({
            opacity: 0
        }, 300);
    }, function() {
        $(".btn-next").animate({
            opacity: 1
        }, 300);
    });

    $(".btn-next").hover(function() {
        $(".btn-prev").animate({
            opacity: 0
        }, 300);
    }, function() {
        $(".btn-prev").animate({
            opacity: 1
        }, 300);
    });

    $(".btn-next").click(function() {
        index++;
        $(".lbview li").eq(index).fadeIn(1000).siblings().fadeOut();
        lis.eq(index).addClass("zi").siblings().removeClass("zi");
    });

    $(".Imgbox").hover(function() {
        clearInterval(t);
    }, function() {
        t = setInterval(play, 3000);
    });

    lis.click(function() {
        index = $(this).index();
        $(".lbview li").eq(index).fadeIn(1000).siblings().fadeOut();
        lis.eq(index).addClass("zi").siblings().removeClass("zi");
    })

})

// 回到顶部
$(function(){
  $("#top").click(function() {
      $("html,body").animate({scrollTop:0}, 500);
  });
})

// 回到底部
$(function(){
  $("#bottom").click(function() {
      $("html,body").animate({scrollTop: document.body.clientHeight}, 500);
  });
})
