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

		if ("" == $("#appointID").val()) {
			obj.ServiceName = "Appointment_AddAppointService";
			obj.SubServiceName = "Appointment_AddAppoint";
			obj.DataServiceName = "Appointment_AddAppoint";
		} else {
			obj.ServiceName = "Appointment_AddAppointService";
			obj.SubServiceName = "Appointment_EditAppoint";
			obj.DataServiceName = "Appointment_EditAppoint";
			obj.appointID = $("#appointID").val();
		}
		
		var element = getCurrentPage();
		obj.serviceType = element.find("#appoint_type").val();
		obj.appointTime = element.find("#appoint_time").val();
		
		wsClient.send(JSON.stringify(obj));
        return false;
	});	

	//编辑模式
	if (sessionStorage.parameters) {
		var param = JSON.parse(sessionStorage.parameters);
		$("#appointID").val(param.id);
		$("button[type='submit']").text("修改");
		sendGetAppointInfoMessage(param.id);

		sessionStorage.removeItem("parameters");
	}
});

function onSelectLoadComplete(name) {

}

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

//发送获取预约信息消息
function sendGetAppointInfoMessage(id) {
	var obj = {};
	obj.ServiceName = "DefaultQueryService";
	obj.SubServiceName = "Appointment_GetAppointInfo";
	obj.DataServiceName = "Appointment_GetAppoint";
	obj.appointID = id;

	wsClient.send(JSON.stringify(obj));	
}