 //当页面加载完成时调用
$(document).ready(function() {
	//查询可用预约类型
	sendGetServiceTypeSelect();

	//默认预约日期
	if($('#appointDate').length) {
		$("#appointDate").datepicker({
			showOtherMonths:true,
			dateFormat: 'yy-mm-dd'
		});
	}

	//默认预约起始时间
	$('#appointTimeStart').timeEntry({
		show24Hours: true,
		spinnerImage: ''
	});

	$('#appointTimeStart').timeEntry('setTime', '00:00');

	//默认预约结束时间
	$('#appointTimeEnd').timeEntry({
		show24Hours: true,
		spinnerImage: ''
	});

	$('#appointTimeEnd').timeEntry('setTime', '23:59');
});

function onSelectLoadComplete(name) {
	switch (name) {
		case "appoint_type":
			sendGetAppointServiceList();
			break;
	}
}

//显示添加服务类型页面
function showAddAppointPanel() {
	openPageOnCurrentTab("appointment/service_addappoint.html")
}

//编辑预约
function editAppoint(e) {
	var obj = {};
	obj.id = $(e).parent().parent().parent().children("td").eq(0).attr("id");
	sessionStorage.parameters = JSON.stringify(obj);

	showAddAppointPanel();
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

//重置搜索项
function resetSearch() {
	$("#appoint_type").val("-1");
	$("#appointState").val("-1");
	$("#appointDate").val("");
	$("#appointTimeStart").val("00:00");
	$("#appointTimeEnd").val("23:59");
}

//发送查询预约类型消息
function sendGetServiceTypeSelect() {
	var obj = {};
	obj.ServiceName = "Appointment_QueryAppointTypeService";
	obj.DataServiceName = "Appointment_GetAppointType";
	wsClient.send(JSON.stringify(obj));
}

//发送查询预约服务消息
function sendGetAppointServiceList() {
	var obj = {};
	obj.ServiceName = "DefaultQueryService";
	obj.SubServiceName = "Appointment_QueryAppointService";
	obj.DataServiceName = "Appointment_GetAppoint";

	if ("-1" != $("#appoint_type").val()) {
		obj.serviceType = $("#appoint_type").val();
	}

	if ("-1" != $("#appointState").val()) {
		obj.serviceState = $("#appointState").val();
	}

	if ("" != $("#appointDate").val()) {
		obj.appointDate = $("#appointDate").val();
	}

	obj.appointTimeStart = $("#appointTimeStart").val();
	obj.appointTimeEnd = $("#appointTimeEnd").val();

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
