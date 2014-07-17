//消息服务处理
function messageService(message) {
	var msgJSON = JSON.parse(message.data);
	
	switch (msgJSON.ServiceName) {
		case "OnLine":
			messageService_OnLine(msgJSON);
			break;
		case "Appointment_QueryServiceTypeService":
			messageService_Appointment_QueryServiceTypeService(msgJSON);
			break;
		case "Appointment_AddServiceTypeService":
			messageService_Appointment_AddServiceTypeService(msgJSON);
			break;
		case "Appointment_QueryAppointTypeService":
			messageService_Appointment_QueryAppointTypeService(msgJSON);
			break;
		case "Appointment_QueryAppointService":
			messageService_Appointment_QueryAppointService(msgJSON);
			break;
		case "Appointment_AddAppointService":
			messageService_Appointment_AddAppointService(msgJSON);
			break;
		case "Appointment_DelServiceTypeService":
			messageService_Appointment_DelServiceTypeService(msgJSON);
			break;
		case "DefaultUpdateService":
			messageService_DefaultUpdateService(msgJSON);
			break;
	}
}

//默认更新服务
function messageService_DefaultUpdateService(message) {
	switch (message.SubServiceName) {
		case "Appointment_AppointOperate":
			messageService_Appointment_AppointOperate(message);
			break;
	}
}

//上线服务
function messageService_OnLine(message) {
	if (STATECODE_SUCCESS == message.StateCode) {
		setTimeout('$("html").removeClass("loadstate")',50);
	}
}

//-----预约服务-----
//查询预约服务服务类型
function messageService_Appointment_QueryServiceTypeService(message) {
	if (STATECODE_SUCCESS == message.StateCode) {
		$("#appointment_ServiceType tbody tr").remove();
		var datas = message.Data;
		var row = "";
		var now = new Date();
		var date = now.getFullYear() + "-" + (1 + now.getMonth()) + "-" + (1 + now.getDate()) + " ";


		if (datas && 0 < datas.length) {
			for (var i=0; i<datas.length; i++) {
				row += '<tr>';

				for (var j=0; j<datas[i].length; j++) {
					switch (j) {
						case 0:
							row += '<td id="' + datas[i][j] + '">' + (i + 1) + '</td>';
							break;
						case 3:
							row += '<td>' + getBusinessDateText(datas[i][j]) + '</td>';
							break;
						case 4:
							row += '<td>' + formatTime(datas[i][j], date) + '-';
							break;
						case 5:
							row += formatTime(datas[i][j], date) + '</td>';
							break;
						case 9:
							row += '<td>' + getTimeBasisText(datas[i][j]) + '</td>';
							break;
						case 10:
							row += '<td>' + getExclusiveText(datas[i][j]) + '</td>';
							break;
						case 11:
							row += '<td>' + getEnableText(datas[i][j]) + '</td>';
							break;
						default:
							row += '<td>' + datas[i][j] + '</td>';
							break;
					}
				}

				row += '<td>';
				row += '<div class="controls center">';
				row += '<a href="#" title="编辑" class="tip" onclick="">';
				row += '<span class="icon12 icomoon-icon-pencil"></span>';
				row += '</a>';
				row += '<a href="#" title="删除" class="tip" onclick="delServiceType(this);">';
				row += '<span class="icon12 icomoon-icon-remove"></span>';
				row += '</a>';
				row += '</div>';
				row += '</td>';
				row += '</tr>';
			}
		} else {
			row = '<td colspan="12" style="text-align: center">无数据</td>';
		}

		$("#appointment_ServiceType tbody").append(row);
	}
}

//新增预约服务服务类型
function messageService_Appointment_AddServiceTypeService(message) {
	if (STATECODE_SUCCESS == message.StateCode) {
		alert("添加预约服务类型成功。");
		closePage();
		setTimeout('sendGetServiceTypeList()', 50);
	} else {
		alert("添加预约服务类型失败！");
	}
}

//查询可用预约类型
function messageService_Appointment_QueryAppointTypeService(message) {
	if (STATECODE_SUCCESS == message.StateCode) {
		$("#appoint_type option").remove();
		var datas = message.Data;
		var option = "";

		if (0 < datas.length) {
			for (var i=0; i<datas.length; i++) {
				option += '<option value="' + datas[i][0] + '">' + datas[i][1] + '</option>';
			}
		} else {
			option += '<option value="0">无数据</option>';
		}

		$("#appoint_type").append(option);
	}
}

//删除预约服务类型
function messageService_Appointment_DelServiceTypeService(message) {
	if (STATECODE_SUCCESS == message.StateCode) {
		alert("删除服务类型成功。");
		sendGetServiceTypeList();
	} else {
		alert("删除服务类型失败！");
	}
}

//查询预约
function messageService_Appointment_QueryAppointService(message) {
	if (STATECODE_SUCCESS == message.StateCode) {
		$("#appointment_appointlist tbody tr").remove();
		var datas = message.Data;
		var row = "";
		var state;

		if (datas && 0 < datas.length) {
			for (var i=0; i<datas.length; i++) {
				row += '<tr>';

				for (var j=0; j<datas[i].length; j++) {
					switch (j) {
						case 0:
							row += '<td id="' + datas[i][j] + '">' + (1 + i) + '</td>';
							break;
						case 1:
							row += '<td type="' + datas[i][j] + '">';
							break;
						case 2:
							row += formatText(datas[i][j]) + '</td>';
							break;
						case 4:
						case 5:
						case 6:
							row += '<td>' + formatTime(datas[i][j], datas[i][3]) + '</td>';
							break;
						case 7:
							state = datas[i][j];
							row += '<td state="' + datas[i][j] + '">' + getAppointStateText(datas[i][j]) + '</td>';
							break;
						default:
							row += '<td>' + formatText(datas[i][j]) + '</td>';
							break;
					}
				}

				row += '<td>';
				row += '<div class="controls center">';

				if ("1" == state) {
					row += '<a href="#" title="抵达" class="tip" onclick="appointOperate(this, 2);">';
					row += '<span class="icon12 icon-flag"></span>';
					row += '</a>';

					row += '<a href="#" title="撤销" class="tip" onclick="appointOperate(this, 3);">';
					row += '<span class="icon12 icomoon-icon-remove"></span>';
					row += '</a>';

					row += '<a href="#" title="修改" class="tip" onclick="">';
				row += '<span class="icon12 icomoon-icon-pencil"></span>';
				row += '</a>';
				}

				row += '</div>';
				row += '</td>';
				row += '</tr>';
			}
		} else {
			row = '<td colspan="8" style="text-align: center">无数据</td>';
		}

		$("#appointment_appointlist tbody").append(row);
	}
}

//新增预约
function messageService_Appointment_AddAppointService(message) {
	if (STATECODE_SUCCESS == message.StateCode) {
		alert("预约成功。");
		closePage();
		setTimeout('sendGetAppointServiceList()', 50);
	} else {
		alert("预约失败！");
	}
}

//预约抵达
function messageService_Appointment_AppointOperate(message) {
	if (STATECODE_SUCCESS == message.StateCode) {
		alert("操作成功。");
		sendGetAppointServiceList();
	} else {
		alert("操作失败！");
	}
}