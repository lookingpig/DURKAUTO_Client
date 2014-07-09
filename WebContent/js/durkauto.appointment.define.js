//当页面加载完成时调用
$(document).ready(function() {
	sendGetServiceTypeList();
});

//显示添加服务类型页面
function showAddServiceTypePanel() {
	openPageOnCurrentTab("appointment/service_addtype.html");
}

//编辑服务类型
function eidtServiceType() {

}

//删除服务类型
function delServiceType(e) {
	var id = $(e).parent().parent().parent().children("td").eq(0).attr("id");
	sendDelServiceType(id);
}

//发送查询已有服务类型消息
function sendGetServiceTypeList() {
	var obj = {};
	obj.ServiceName = "Appointment_QueryServiceTypeService";
	wsClient.send(JSON.stringify(obj));
}

//发送删除指定服务类型消息
function sendDelServiceType(id) {
	var obj = {};
	obj.ServiceName = "Appointment_DelServiceTypeService";
	obj.id = id;
	wsClient.send(JSON.stringify(obj));	
}