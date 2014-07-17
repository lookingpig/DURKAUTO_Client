 //当页面加载完成时调用
$(document).ready(function() {
	sendGetAppointServiceList();
});

//显示添加服务类型页面
function showAddAppointPanel() {
	openPageOnCurrentTab("appointment/service_addappoint.html")
}

//预约抵达
function appointOperate(e, state) {
	var currentState = $(e).parent().parent().parent().children("td").eq(6).attr("state");

	if ("1" == currentState)  {
		var id = $(e).parent().parent().parent().children("td").eq(0).attr("id");
		var now = new Date();
		var time = now.getHours() + ":" + now.getMinutes();

		sendAppointOperateeMessage(id, time, state);
	}
}

//发送查询预约服务消息
function sendGetAppointServiceList() {
	var obj = {};
	obj.ServiceName = "Appointment_QueryAppointService";
	wsClient.send(JSON.stringify(obj));
}

//发送预约抵达消息
function sendAppointOperateeMessage(id, time, state) {
	var obj = {};
	obj.ServiceName = "DefaultUpdateService";
	obj.SubServiceName = "Appointment_AppointOperate";
	obj.DataServiceName = "Appointment_AppointOperate";
	obj.appointID = id;
	obj.arrivalTime = time;
	obj.appointState = state;

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
