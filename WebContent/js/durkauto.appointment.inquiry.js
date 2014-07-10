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

//获得预约状态文本
function getAppointStateText(state) {
	switch (state) {
		case '1':
			return '预约';
			break;
		case '2':
			return '抵达';
			break;
		case '3':
			return '撤销';
			break;
		case '4':
			return '过期';
			break;
	}
}
