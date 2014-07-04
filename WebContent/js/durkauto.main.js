//全局变量
var WEBSOCKET_HOST = "ws://localhost:8080/DurkAutoClient/service/wsService";
var wsClient;

//当页面加载完成时调用
$(document).ready(function() {
	wsClient = connectWS(WEBSOCKET_HOST);
});
	
//当主菜单被选中时加载相应页面
function onMenuSelect(e) {
	var url = "";
	var servicename = $(e).attr("name");
	var tabname = $(e).text();

	switch (servicename) {
		case "appointment-inquiry" :
		url = "appointment/service_inquiry.html";
		break;
		case "appointment-define" :
		url = "appointment/service_define.html";
		break;
	}

	if ("" != url) {
		if (0 < $("#" + servicename).length) {
			//在这里如果已经存在该页面则切换
		} else {
			$("#main_tab").append('<li><a href="#' + servicename + '" data-toggle="tab" id="' + servicename + '_tab">' + tabname + ' <span class=" icon-remove" onclick="closeMainTab(this);"></span></a></li>');
			$("#main_panel").append('<div class="tab-pane fade" id="' + servicename + '"></div>');
			$("#" + servicename).load(url);
		}
	}
}

//关闭主标签栏
function closeMainTab(e) {
	var id = $(e).parent().attr("id");
	//删除标签
	$("#" + id).remove();
	//删除页面
	id = id.substring(0, id.indexOf("_tab"));
	$("#" + id).remove();
}