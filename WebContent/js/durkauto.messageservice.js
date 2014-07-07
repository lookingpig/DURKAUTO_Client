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

		for (var i=0; i<datas.length; i++) {
			row += '<tr>';

			for (var j=0; j<datas[i].length; j++) {
				
				if (4 == j) {
					row += '<td>' + datas[i][j] + '-';
				} else if (5 == j) {
					row += datas[i][j] + '</td>';
				} else {
					row += '<td>' + datas[i][j] + '</td>';
				}
			}

			row += '<td><div class="controls center"><a href="#" title="Edit task" class="tip"><span class="icon12 icomoon-icon-pencil"></span></a> 		<a href="#" title="Remove task" class="tip"><span class="icon12 icomoon-icon-remove"></span></a></div></td>';
			row += '</tr>';
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