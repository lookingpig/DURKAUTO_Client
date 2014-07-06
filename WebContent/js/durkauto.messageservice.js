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
		var data = JSON.parse(message.data);
		var row = "";

		for (var i=0; i<message.length; i++) {
			row += '<tr>';

			for (var j=0; j<message[i].length; j++) {
				row += '<td>' + message[i][j] + '</td>';
			}

			row += '<td><div class="controls center"><a href="#" title="Edit task" class="tip"><span class="icon12 icomoon-icon-pencil"></span></a> 		<a href="#" title="Remove task" class="tip"><span class="icon12 icomoon-icon-remove"></span></a></div></td>';
			row += '</tr>';
		}

		$("#appointment_ServiceType tbody").append(row);
	}
}

