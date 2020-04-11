//JQUERY加载完成后
$(document).ready(function(){

  // 导航分类的显示与隐藏
  // 品牌分类
  $("#hd-desc").mouseover(function(){
    $("hd-desc-sub").show();
  });

  $("#hd-desc").mouseout(function(){
    $("hd-desc-sub").hide();
  });

});