$(function($) {
	$("#loginForm").submit(function(){
		var obj = {};
		obj.ServiceName = "Login";
		obj.username = $("#username").val();
		obj.password = $("#password").val();

		$.ajax({
            type : 'post',
            url : 'Service',
            data: 'Message=' + JSON.stringify(obj),
            dataType : 'json',
            async : false,
            success : function(data) {
                if ("000000000" == data.StateCode) {
					location.href = "index.jsp";
				} else {
					alert("Fall code: " + data.StateCode);
				}
            },
            error : function() {  
                alert("登陆失败！");
            }  
        });

        return false;
	});	
});