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
	}
}

//上线服务
function messageService_OnLine(message) {
	if (STATECODE_SUCCESS == message.StateCode) {
		setTimeout('$("html").removeClass("loadstate")',500);
	}
}

//查询预约服务服务类型
function messageService_Appointment_QueryServiceTypeService(message) {
	if (STATECODE_SUCCESS == message.StateCode) {
		$("#appointment_ServiceType tbody tr").remove();
		var datas = message.Data;
		var row = "";

		if (0 < datas.length) {
			for (var i=0; i<datas.length; i++) {
				row += '<tr>';

				for (var j=0; j<datas[i].length; j++) {
					switch (j) {
						case 0:
							row += '<td id="' + datas[i][j] + '">' + i + '</td>';
							break;
						case 4:
							row += '<td>' + datas[i][j] + '-';
							break;
						case 5:
							row += datas[i][j] + '</td>';
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
			row = '<td colspan="11">无数据</td>';
		}

		$("#appointment_ServiceType tbody").append(row);
	}
}

//新增预约服务服务类型
function messageService_Appointment_AddServiceTypeService(message) {
	if (STATECODE_SUCCESS == message.StateCode) {
		alert("添加预约服务类型成功。");
		closePage();
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

//查询预约
function messageService_Appointment_QueryAppointService(message) {
	if (STATECODE_SUCCESS == message.StateCode) {
		$("#appointment_appointlist tbody tr").remove();
		var datas = message.Data;
		var row = "";

		if (0 < datas.length) {
			for (var i=0; i<datas.length; i++) {
				row += '<tr>';

				for (var j=0; j<datas[i].length; j++) {
					switch (j) {
						case 1:
							row += '<td id="' + datas[i][j] + '">';
							break;
						case 2:
							row += datas[i][j] + '</td>';
							break;
						case 4:
							row += '<td>' + formatTime(datas[i][j-1] + " " + datas[i][j]) + '</td>';
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
			row = '<td colspan="8">无数据</td>';
		}

		$("#appointment_appointlist tbody").append(row);
	}
}

//新增预约
function messageService_Appointment_AddAppointService(message) {
	if (STATECODE_SUCCESS == message.StateCode) {
		alert("预约成功。");
		closePage();
	} else {
		alert("预约失败！");
	}
}