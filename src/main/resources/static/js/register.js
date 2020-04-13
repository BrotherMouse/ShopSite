// 验证用户
function checkUser(){
	var account = document.getElementById('account').value;
	var testAccount = document.getElementById('testAccount');
	if (account.length == 0) {
		testAccount.innerHTML = '* 用户名不能为空';
        testAccount.style.color = '#f00';
        return false;
	}else if (account.match(/^[\D][\w]{4,11}$/) == null) {
    	 testAccount.innerHTML = '* 用户名必须首位为非数字，长度为5-12.';
         testAccount.style.color = '#f00';
         return false;
    } else {
         testAccount.innerHTML = '* 通过验证.';
         testAccount.style.color = '#0a0';
         return true;
     }
}

// 验证密码
function checkPassword(){
	var password = document.getElementById('password').value;
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
function checkPassword2(){
	var password = document.getElementById('password').value;
	var password2 = document.getElementById('password2').value;
	var testPassword2 = document.getElementById('testPassword2');
	if (password2.length == 0) {
		testPassword2.innerHTML = '* 不能为空';
		testPassword2.style.color = '#f00';
		return false;
	}else if(password2 !== password){
		testPassword2.innerHTML = '* 密码不一致';
		testPassword2.style.color = '#f00';
		return false;
	}else {
		testPassword2.innerHTML = '* 密码一致，通过验证';
		testPassword2.style.color = '#0a0';
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
 		var account = $("#account").val();
 		var password = $("#password").val();
 		var name = $("#name").val();
 		var email = $("#e-mail").val();
 		var cellphone = $("#phone").val();

 		if(account == "" || password == "" || name == "" || email == "" || cellphone == ""){
 			alert("请填写完整的注册信息");
 			return;
 		}

        var formData = new FormData();
        formData.append("account", account);
        formData.append("password", password);
        formData.append("name", name);
        formData.append("email", email);
        formData.append("cellphone", cellphone);
 		$.ajax({
 			type: "post",
 			url: "registerUser",
 			data: formData,
            processData : false,
            contentType : false,
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
 