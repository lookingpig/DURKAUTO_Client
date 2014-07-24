//当前查询项
var searchOption = {};

//图表选项
var options = {
	grid: {
		show: true,
	    aboveData: true,
	    color: "#3f3f3f" ,
	    labelMargin: 5,
	    axisMargin: 0, 
	    borderWidth: 0,
	    borderColor:null,
	    minBorderMargin: 5 ,
	    clickable: true, 
	    hoverable: true,
	    autoHighlight: true,
	    mouseActiveRadius: 20
	},
    series: {
    	grow: {active:false},
        lines: {
    		show: true,
    		fill: true,
    		lineWidth: 2,
    		steps: false
        	},
        points: {show:false}
    },
    legend: { position: "se" },
    yaxis: { min: 0 },
    xaxis: {
    	mode: "time", 
    	timeformat: "%y-%m-%d",
    	minTickSize: [3, "day"]
    },
    colors: ['#88bbc8', '#6048FF', '#DFFF6A', '#FF4141'],
    shadowSize:1,
    tooltip: true,
	tooltipOpts: {
		content: "%x %s : %y",
		shifts: {
			x: -30,
			y: -50
		}
	}
};

//当页面加载完成时调用
$(document).ready(function() {
	//查询可用预约类型
	sendGetServiceTypeSelect();

	if($('#startTime').length) {
		$("#startTime").datepicker({
			showOtherMonths:true,
			dateFormat: 'yy-mm-dd'
		});
	}

	if($('#endTime').length) {
		$("#endTime").datepicker({
			showOtherMonths:true,
			dateFormat: 'yy-mm-dd'
		});
	}

	resetSearch();
});

function onSelectLoadComplete(name) {
	
	switch (name) {
		case "appoint_type":
			statistics();
			break;
	}
}

//搜索
function statistics() {
	
	if (30 < betweenDay($("#startTime").val(), $("#endTime").val())) {
		alert("一次最多统计30天的数据！");
		return;
	}
	
	sendQueryAppointVisitMessage();
}

//重置搜索项
function resetSearch() {
	//默认统计日期
	var now = new Date();
	var start = minusDay(now, 29);
	$("#startTime").val(formatDate(start));
	$("#endTime").val(formatDate(now));
	$("#appoint_type").val("-1");
}

//显示图表
function showChart(total, arrive, cancel, overdue) {
	if (total  && 0 < total.length) {
		options.xaxis.minTickSize[0] = parseInt(0 == total.length % 10 ? total.length / 10 : total.length / 10 + 1);

		$.plot($(".lines-chart"), [ 
			{
				label: "使用量", 
				data: total,
				lines: {fillColor: "#A1DEEE"},
				points: {fillColor: "#C9EEF7"}
			}, 
			{
				label: "抵达量", 
				data: arrive,
				lines: {fillColor: "#9889FE"},
				points: {fillColor: "#BAB0FE"}
			}, 
			{
				label: "撤销量", 
				data: cancel,
				lines: {fillColor: "#EAFF9C"},
				points: {fillColor: "#F4FDD3"}
			}, 
			{
				label: "过期量", 
				data: overdue,
				lines: {fillColor: "#FF7C7C"},
				points: {fillColor: "#FFB9B9"}
			}
		], options);
	}
}

//发送查询预约类型消息
function sendGetServiceTypeSelect() {
	var obj = {};
	obj.ServiceName = "Appointment_QueryAppointTypeService";
	obj.DataServiceName = "Appointment_GetAppointType";
	wsClient.send(JSON.stringify(obj));
}

//发送查询预约服务访问量消息
function sendQueryAppointVisitMessage() {
	var obj = {};
	obj.ServiceName = "DefaultQueryService";
	obj.SubServiceName = "Statistics_QueryAppointVisit";
	obj.DataServiceName = "Statistics_GetAppointVisit";
	obj.startTime = $("#startTime").val();
	obj.endTime = $("#endTime").val();
	
	if ("-1" != $("#appoint_type").val()) {
		obj.appointType = $("#appoint_type").val();
	}

	searchOption = obj;

	wsClient.send(JSON.stringify(obj));
}