$(document).ready(function(){
	$("#loginBtn").on("click",function(){
		var account = $("#account").val();
		var password = $("#password").val();

		if(account == "" || password == "") {
		    alert("请输入用户名和密码");
		    return;
		}

        var formData = new FormData();
        formData.append("account", account);
        formData.append("password", password);
		$.ajax({
			type: "post",
 			url: "checkLogin",
 			data: formData,
            processData : false,
            contentType : false,
			success: function(data){
				if (data.result == "NotExist") {
				    alert("账号不存在");
				}else if(data.result == "Verified") {
				    location.href = "/index";
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