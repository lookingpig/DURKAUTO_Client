//当页面加载完成时调用
$(document).ready(function() {
	//查询已有服务类型
	var obj = {};
	obj.ServiceName = "Appointment_QueryServiceTypeService";
	wsClient.send(JSON.stringify(obj));
});

//显示添加服务类型页面
function showAddServiceTypePanel() {
	openPageOnCurrentTab("appointment/service_addtype.html")
}