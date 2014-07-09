 //当页面加载完成时调用
$(document).ready(function() {
	//查询可用预约类型
	sendGetServiceTypeSelect();

	//默认预约时间
	$('#appoint_time').timeEntry({
		show24Hours: true,
		spinnerImage: ''
	});

	var now = new Date();
	$('#appoint_time').timeEntry('setTime', now.getHours() + ":" + now.getMinutes());
	
	$("#appointForm").submit(function(){
		var obj = {};
		obj.ServiceName = "Appointment_AddAppointService";
		obj.serviceType = $("#appoint_type").val();
		obj.appointTime = $("#appoint_time").val();
		
		wsClient.send(JSON.stringify(obj));
        return false;
	});	
});

//关闭当前页面
function closePage() {
	$("#" + current_page).children("div[id=" + current_sub_page + "]").remove();
	$("#" + current_page).children("div").first().show();
}

//发送查询预约类型消息
function sendGetServiceTypeSelect() {
	var obj = {};
	obj.ServiceName = "Appointment_QueryAppointTypeService";
	obj.DataServiceName = "Appointment_GetAppointType";
	wsClient.send(JSON.stringify(obj));
}