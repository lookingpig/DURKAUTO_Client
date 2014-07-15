//当页面加载完成时调用
$(document).ready(function() {
	//默认起始时间
	$('#business_time_start').timeEntry({
		show24Hours: true,
		spinnerImage: ''
	});

	$('#business_time_start').timeEntry('setTime', '09:00');

	//默认结束时间
	$('#business_time_end').timeEntry({
		show24Hours: true,
		spinnerImage: ''
	});

	$('#business_time_end').timeEntry('setTime', '18:00');

	$(".ibuttonCheck").iButton({
		 labelOn: "<span class='icon16 icomoon-icon-checkmark-2 white'></span>",
		 labelOff: "<span class='icon16 icomoon-icon-cancel-3 white'></span>",
		 enableDrag: false
	});

	$("#ServiceTypeForm").submit(function(){
		//营业日期换算
		var businessDate = 0;

		$("input[name='business_date']").each(function(){

			if ("checked" == $(this).attr("checked")) {
				businessDate = businessDate | $(this).val();
			}
		});

		var obj = {};
		obj.ServiceName = "Appointment_AddServiceTypeService";
		obj.typeName = $("#type_name").val();
		obj.parking = $("#parking").val();
		obj.businessDate = businessDate;
		obj.businessHoursStart = $("#business_time_start").val();
		obj.businessHoursEnd = $("#business_time_end").val();
		obj.serviceTime = $("#service_time").val();
		obj.reminderTime = $("#reminder_time").val();
		obj.waitTime = $("#Wait_time").val();
		obj.timeBasis = $("#time_basis").val();
		obj.exclusive = "checked" == $("#exclusive").attr("checked") ? true : false;
		obj.enable = "checked" == $("#enable").attr("checked") ? true : false;

		wsClient.send(JSON.stringify(obj));
        return false;
	});	

	$("input, textarea, select").not('.nostyle').uniform();
});

//关闭当前页面
function closePage() {
	$("#" + current_page).children("div[id=" + current_sub_page + "]").remove();
	$("#" + current_page).children("div").first().show();
}