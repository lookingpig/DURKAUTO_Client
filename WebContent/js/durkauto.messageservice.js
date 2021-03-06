//消息服务处理
function messageService(message) {
	var msgJSON = JSON.parse(message.data);
	
	switch (msgJSON.ServiceName) {
		case "DefaultUpdateService":
			messageService_DefaultUpdateService(msgJSON);
			break;
		case "DefaultQueryService":
			messageService_DefaultQueryService(msgJSON);
			break;
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
		case "Appointment_AddAppointService":
			messageService_Appointment_AddAppointService(msgJSON);
			break;
		case "Appointment_DelServiceTypeService":
			messageService_Appointment_DelServiceTypeService(msgJSON);
			break;
	}
}

//默认更新服务
function messageService_DefaultUpdateService(message) {
	switch (message.SubServiceName) {
		case "Appointment_TypeOperate":
			messageService_Appointment_TypeOperate(message);
			break;
		case "Appointment_AppointOperate":
			messageService_Appointment_AppointOperate(message);
			break;
	}
}

//默认查询服务
function messageService_DefaultQueryService(message) {
	switch (message.SubServiceName) {
		case "Appointment_QueryAppointService":
			messageService_Appointment_QueryAppointService(message);
			break;
		case "Appointment_GetTypeInfo":
			messageService_Appointment_GetTypeInfo(message);
			break;
		case "Appointment_GetAppointInfo":
			messageService_Appointment_GetAppointInfo(message);
			break;
		case "Statistics_QueryAppointVisit":
			messageService_Statistics_QueryAppointVisit(message);
			break;
	}
}

//上线服务
function messageService_OnLine(message) {
	if (STATECODE_SUCCESS == message.StateCode) {
		setTimeout('$("html").removeClass("loadstate")', 50);
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
		var state;

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
						case 11:
							row += '<td>' + getTimeBasisText(datas[i][j]) + '</td>';
							break;
						case 12:
							row += '<td>' + getExclusiveText(datas[i][j]) + '</td>';
							break;
						case 13:
							state = datas[i][j];
							row += '<td state="' + datas[i][j] + '">' + getEnableText(datas[i][j]) + '</td>';
							break;
						default:
							row += '<td>' + datas[i][j] + '</td>';
							break;
					}
				}

				row += '<td>';
				row += '<div class="controls center">';

				if ("1" == state) {
					row += '<a href="#" title="禁用" class="tip" onclick="changeTypeState(this, false);">';
					row += '<span class="icon12 icon-remove"></span>';
				} else {
					row += '<a href="#" title="启用" class="tip" onclick="changeTypeState(this, true);">';
					row += '<span class="icon12 icon-ok"></span>';
				}

				row += '</a>';
				row += '<a href="#" title="编辑" class="tip" onclick="eidtServiceType(this);">';
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
			row = '<tr><td colspan="14" style="text-align: center">无数据</td></tr>';
		}

		$("#appointment_ServiceType tbody").append(row);
	}
}

//新增预约服务服务类型
function messageService_Appointment_AddServiceTypeService(message) {
	if (STATECODE_SUCCESS == message.StateCode) {
		if ("Appointment_AddServiceType" == message.SubServiceName) {
			alert("添加预约服务类型成功。");	
		} else {
			alert("更新预约服务类型成功。");
		}
		
		closePage();
		setTimeout('sendGetServiceTypeList()', 50);
	} else {
		if ("Appointment_AddServiceType" == message.SubServiceName) {
			alert("添加预约服务类型失败！");
		} else {
			alert("更新预约服务类型失败！");
		}
	}
}

//查询可用预约类型
function messageService_Appointment_QueryAppointTypeService(message) {
	if (STATECODE_SUCCESS == message.StateCode) {
		var element = $("#" + current_page + " #" + current_sub_page).find("#appoint_type");
		element.children("option").remove();
		var datas = message.Data;
		var option = "";

		option += getSelectModelOption(element.attr("model"));

		if (0 < datas.length) {
			for (var i=0; i<datas.length; i++) {
				option += '<option value="' + datas[i][0] + '">' + datas[i][1] + '</option>';
			}
		} else {
			option += '<option value="-2">无数据</option>';
		}

		element.append(option);
		onSelectLoadComplete("appoint_type");
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

//服务类型操作
function messageService_Appointment_TypeOperate(message) {
	if (STATECODE_SUCCESS == message.StateCode) {
		alert("操作成功。");
		sendGetServiceTypeList();
	} else {
		alert("操作失败！");
	}	
}

//获得服务类型信息
function messageService_Appointment_GetTypeInfo(message) {
	if (STATECODE_SUCCESS == message.StateCode && message.Data && 1 == message.Data.length) {
		$("#type_name").val(message.Data[0][1]);
		$("#parking").val(message.Data[0][2]);

		for (var i=0; i<7; i++) {
			if (Math.pow(2, i) & message.Data[0][3]) {
				$("input[name='business_date']").eq(i).attr("checked", "checked");
			} else {
				$("input[name='business_date']").eq(i).removeAttr("checked");
			}
		}

		$("#business_time_start").val(formatTime(message.Data[0][4]));
		$("#business_time_end").val(formatTime(message.Data[0][5]));
		$("#service_time").val(message.Data[0][6]);
		$("#reminder_time").val(message.Data[0][7]);
		$("#Wait_time").val(message.Data[0][8]);
		$("#time_basis").val(message.Data[0][9]);
		
		if ("0" == message.Data[0][10]) {
			$("#exclusive").click();
		}

		if ("0" == message.Data[0][11]) {
			$("#enable").click();
		}

		updateComponent();
	} else {
		alert("获取类型信息失败，无法编辑！");
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
						case 7:
							row += '<td>' + formatTime(datas[i][j], datas[i][3]) + '</td>';
							break;
						case 8:
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

					row += '<a href="#" title="修改" class="tip" onclick="editAppoint(this);">';
					row += '<span class="icon12 icomoon-icon-pencil"></span>';
					row += '</a>';
				}

				row += '</div>';
				row += '</td>';
				row += '</tr>';
			}
		} else {
			row = '<tr><td colspan="9" style="text-align: center">无数据</td></tr>';
		}

		$("#appointment_appointlist tbody").append(row);
	}
}

//新增预约
function messageService_Appointment_AddAppointService(message) {
	if (STATECODE_SUCCESS == message.StateCode) {
		if ("Appointment_AddAppoint" == message.SubServiceName) {
			alert("预约成功。");
		} else {
			alert("更新预约成功。");
		}
		
		closePage();
		setTimeout('sendGetAppointServiceList()', 50);
	} else {
		if ("Appointment_AddAppoint" == message.SubServiceName) {
			alert("预约失败！");
		} else {
			alert("更新预约失败！");
		}
	}
}

//预约操作
function messageService_Appointment_AppointOperate(message) {
	if (STATECODE_SUCCESS == message.StateCode) {
		alert("操作成功。");
		sendGetAppointServiceList();
	} else {
		alert("操作失败！");
	}
}

//查询预约信息
function messageService_Appointment_GetAppointInfo(message) {
	if (STATECODE_SUCCESS == message.StateCode && message.Data && 1 == message.Data.length) {
		$("#appoint_type").val(message.Data[0][1]);
		$("#appoint_time").val(formatTime(message.Data[0][4]));
	} else {
		alert("获取预约信息失败，无法编辑！");
	}
}

//查询预约服务访问量
function messageService_Statistics_QueryAppointVisit(message) {
	if (STATECODE_SUCCESS == message.StateCode) {
		var start = new Date(searchOption.startTime).getTime();
		var end = new Date(searchOption.endTime).getTime();
		var step = 24 * 60 * 60 * 1000;
		
		var datas = message.Data;
		var index = 0;
		var date = datas && index < datas.length ? new Date(datas[index][0]).getTime() : 0;

		var total = new Array();
		var arrive = new Array();
		var cancel = new Array();
		var overdue = new Array();

		while (start <= end) {
			
			if (date == start) {
				total.push([start, datas[index][1]]);
				arrive.push([start, datas[index][2]]);
				cancel.push([start, datas[index][3]]);
				overdue.push([start, datas[index][4]]);

				date = ++index < datas.length ? new Date(datas[index][0]).getTime() : 0;
			} else {
				total.push([start, 0]);
				arrive.push([start, 0]);
				cancel.push([start, 0]);
				overdue.push([start, 0]);
			}

			start += step;
		}

		showChart(total, arrive, cancel, overdue);
	}
}