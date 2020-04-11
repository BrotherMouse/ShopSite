$(document).ready(function(){
	$("#loginBtn").on("click",function(){
		var userName = $("#userName").val();
		var pwd = $("#passWord").val();
		
		
		$.ajax({
			type: "post",
 			url: "/checklogin",
 			data: "{\"account\":\""+userName+"\",\"password\":\""+pwd+"\"}",
 			contentType: "application/json;charset=utf-8",
 			dataType: "json",
			success: function(data){
				if (data.result == "NotExist") {
                					alert("账号不存在");
                				}else if(data.result == "Verified"){
                                    location.href = "index";
                                }else{
                                    alert("密码不正确");
                                }


                			},
                			error: function(err){
                				alert(err);
                			}

		});
	});

});

/*$(document).ready(function () {
    $('#loginBtn').click(function () {
        if ($('#userName').val() == "" || $('#passWord').val() == "") {
            alert("用户名或密码不能为空！");
        }
        else {
            $.ajax({
                type: "POST",
                url: "Ajax/login.aspx",
                data: "username=" + escape($('#userName').val()) + "&password=" + escape($('#passWord').val()),
                /*beforeSend: function () {
                    $("#loading").css("display", "block"); //点击登录后显示loading，隐藏输入框
                    $("#login").css("display", "none");
                },*/
               /* success: function (msg) {
                   // $("#loading").hide(); //隐藏loading
                    if (msg == "success") {
                        //parent.tb_remove();
                        parent.document.location.href = "index.html"; //如果登录成功则跳到管理界面
                        //parent.tb_remove();
                    }
                    if (msg == "fail") {
                        alert("登录失败！");
                    }
                },
                /*complete: function (data) {
                    $("#loading").css("display", "none"); //点击登录后显示loading，隐藏输入框
                    $("#login").css("display", "block");
                },*/
              /*  error: function (XMLHttpRequest, textStatus, thrownError) {
                }
            });
        }
    });
});*/



/*$("#loginBtn").click(function () {
        var name = $("#userName").val(); //获取输入的用户名
        var pwd = $("#passWord").val(); //获取输入的密码
        if (name=="") {              //如果没有数据，就return掉
            alert("用户名不能为空");
            return;
        }
        if (pwd == "") {
            alert("密码不能为空");
            return;
        }
 
 
        $.ajax({       
            type: "post",
            url: "login.aspx",
            data: { username: name, userpwd: pwd },
            success: function (data) {
                if (data=="true") {   //返回的是true，弹窗提示成功，并跳转到首页
                    alert("登陆成功");
                    location.href = "index.aspx";
                }
                else {
                    alert("登陆失败");
                }
            }
        });
    })*/
