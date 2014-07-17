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

//改变服务类型状态
function changeTypeState(e, state) {
	var currentState = "1" == $(e).parent().parent().parent().children("td").eq(10).attr("state") ? true : false;

	if (currentState != state) {
		var id = $(e).parent().parent().parent().children("td").eq(0).attr("id");
		sendTypeOperateMessage(id, state);
	}
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

//发送服务类型操作消息
function sendTypeOperateMessage(id, state) {
	var obj = {};
	obj.ServiceName = "DefaultUpdateService";
	obj.SubServiceName = "Appointment_TypeOperate";
	obj.DataServiceName = "Appointment_ChangeTypeState";
	obj.typeID = id;
	obj.enable = state;

	wsClient.send(JSON.stringify(obj));	
}

//获得营业日期文本
function getBusinessDateText(date) {
	var text = "";
	var start = 0;
	var end = 0;

	for (var i=0; i<7; i++) {

		//当天标记
		if (Math.pow(2, i) & date) {
			if (0 == end) {
				start = i + 1;
				end = start;
			} else if (i == end) {
				end = i + 1;
			} else {
				text += getWeekIntervalText(start, end) + "，";
				start = i + 1;
				end = start;
			}
		}
	}
	
	text += getWeekIntervalText(start, end);
	return text;
}

//获得周区间文本
function getWeekIntervalText(start, end) {
	if (start == end) {
		return "周" + getWeekTextText(start);
	} else if (start < end) {
		return "周" + getWeekTextText(start) + "至周" + getWeekTextText(end);
	} else {
		return "";
	}
}

//获得周文本
function getWeekTextText(week) {
	switch (week) {
		case 1:
			return "一";
			break;
		case 2:
			return "二";
			break;
		case 3:
			return "三";
			break;
		case 4:
			return "四";
			break;
		case 5:
			return "五";
			break;
		case 6:
			return "六";
			break;
		case 7:
			return "日";
			break;
		default:
			return "";
			break;
	}
}

//获得时间单位文本
function getTimeBasisText(basis) {
	switch (basis) {
		case "d":
			return "天";
			break;
		case "h":
			return "时";
			break;
		case "m":
			return "分";
			break;
		case "s":
			return "秒";
			break;
		default:
			return "";
			break;
	}
}

//获得独享状态文本
function getExclusiveText(exclusive) {
	switch (exclusive) {
		case "0":
			return "共享";
			break;
		case "1":
			return "独享";
			break;
		default:
			return "";
			break;
	}
}

//获得启用状态文本
function getEnableText(enable) {
	switch (enable) {
		case "0":
			return "禁用";
			break;
		case "1":
			return "启用";
			break;
		default:
			return "";
			break;
	}
}