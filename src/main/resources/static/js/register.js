

// 验证用户
function checkUser(){
	var username = document.getElementById('userName').value;
	var testname = document.getElementById('testName');
	if (username.length == 0) {
		testname.innerHTML = '* 用户名不能为空';
        testname.style.color = '#f00';
        return false;
	}else if (username.match(/^[\D][\w]{4,11}$/) == null) {
    	 testname.innerHTML = '* 用户名必须首位为非数字，长度为5-12.';
         testname.style.color = '#f00';
         return false;
    } else {
         testname.innerHTML = '* 通过验证.';
         testname.style.color = '#0a0';
         return true;
     }
}

// 验证密码
function checkPassword(){
	var password = document.getElementById('passWord').value;
	var testpass = document.getElementById('testPass');
	var reg = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,15}$/;
if (password.length == 0) {
	testpass.innerHTML = '* 密码不能为空';
	testpass.style.color = '#f00';
	return false;

}else if (reg.test(password)) {
	
	testpass.innerHTML = '* 通过验证';
    testpass.style.color = '#0a0';
    return true;

}else {
    testpass.innerHTML = '* 密码必须是以数字和字母组合的8-16位数';
	testpass.style.color = '#f00';
	return false;
}
}

// 验证再次输入密码
function checkRepsd(){
	var password = document.getElementById('passWord').value;
	var repsd = document.getElementById('repassWord').value;
	var testRepsd = document.getElementById('testRepsd');
	if (password.length == 0) {
		testRepsd.innerHTML = '* 不能为空';
		testRepsd.style.color = '#f00';
		return false;
		
	}else if(repsd !== password){
		
		testRepsd.innerHTML = '* 密码不一致';
		testRepsd.style.color = '#f00';
		return false;

	}else {
		testRepsd.innerHTML = '* 密码一致，通过验证';
		testRepsd.style.color = '#0a0';
		return true;
	}
}
// 验证邮箱
function checkEmail(){
	var email = document.getElementById('e-mail').value;
	var testemail = document.getElementById('testEmail');
	var re = /^[a-zA-Z0-9-._]+[@][a-zA-Z0-9-._]+\.(com|cn|net)/;
	if (email.length == 0) {
		testemail.innerHTML = '* 邮箱不能为空';
		testemail.style.color = '#f00';
		return false;
	}
	else if (re.test(email)) {
		testemail.innerHTML = '* 通过验证';
		testemail.style.color = '#0a0';
		return true;
	}else {
		testemail.innerHTML = '* 邮箱格式不正确';
		testemail.style.color = '#f00';
		return false;
	}

}
// 验证手机号码
function checkPhone(){
	var phone = document.getElementById('phone').value;
	var testphone = document.getElementById('testPhone');
	var reg = /^1[3|5|7|8]\d{9}/;
	if (phone.length == 0) {
		testPhone.innerHTML = '* 手机号码不能为空';
		testPhone.style.color = '#f00';
		return false;
	}else if (reg.test(phone)) {
		testPhone.innerHTML = '* 通过验证';
		testPhone.style.color = '#0a0';
		return true;
	}else {
		testPhone.innerHTML = '* 手机号码不合法';
		testPhone.style.color = '#f00';
		return false;
	}

}




//提交
 $(document).ready(function(){
 	$("#submitBtn").on("click", function(){
 		var uID = $("#userName").val();
 		var uPwd = $("#passWord").val();
 		var testname = $("#testName").val();
 		if(uID == "" || uPwd == ""){
 			alert("提交失败");
 			return;
 		}

 		$.ajax({
 			type: "post",
 			url: "/registeruser",
 			data: "{\"account\":\""+uID+"\",\"password\":\""+uPwd+"\"}",
 			contentType: "application/json;charset=utf-8",
 			dataType: "json",
 			async: true,
 			success: function(data) {
 				if (data.result == "Success") {
                                					alert("注册成功！");
                                					 location.href = "login";

                                                }else{
                                                    alert("用户已存在！");
                                                }


                                			},
                                			error: function(err){
                                				alert(err);
                                			}
 		});

 	});

 }); 
 