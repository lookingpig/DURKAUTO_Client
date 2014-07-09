 //当页面加载完成时调用
$(document).ready(function() {
	sendGetAppointServiceList();
});

//显示添加服务类型页面
function showAddAppointPanel() {
	openPageOnCurrentTab("appointment/service_addappoint.html")
}

//发送查询预约服务消息
function sendGetAppointServiceList() {
	var obj = {};
	obj.ServiceName = "Appointment_QueryAppointService";
	wsClient.send(JSON.stringify(obj));
}
