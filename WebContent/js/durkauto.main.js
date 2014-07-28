//全局变量
var WEBSOCKET_HOST = "/DurkAutoClient/service/wsService";
var STATECODE_SUCCESS = "000000000";
var wsClient = null;
var current_tab = null;
var current_page = null;
var current_sub_page = null;

//当页面加载完成时调用
$(document).ready(function() {
	if (null == wsClient) {
		wsClient = connectWS(WEBSOCKET_HOST);
	} else {
		setTimeout('$("html").removeClass("loadstate")', 50);
	}
});

//当退出登录时调用
function onLogout() {
	var obj = {};
	obj.ServiceName = "Logout";

	$.ajax({
        type : 'post',
        url : 'Service',
        data: 'Message=' + JSON.stringify(obj),
        dataType : 'json',
        async : false,
        success : function(data) {
            if (STATECODE_SUCCESS == data.StateCode) {
				wsClient.close();
				sessionStorage.wsClient = null;
				location.href = "login.html";
			} else {
				alert("退出登录失败！代码：" + data.StateCode);
			}
        },
        error : function() {  
            alert("登陆失败！");
        }  
    });
}
	
//当主菜单被点击时调用
function onMainMenuSelect(e) {
	var url = "";
	var pageName = $(e).attr("name");
	var tabname = $(e).text();

	switch (pageName) {
		case "appointment-inquiry" :
			url = "appointment/service_inquiry.html";
			break;
		case "appointment-define" :
			url = "appointment/service_define.html";
			break;
		case "statistics-appointVisit":
			url = "statistics/appoint_visit_quantity.html";
			break;
	}

	if ("" != url) {
		openPage(tabname, pageName, url);
	}
}

//当主菜单被选中时加载相应页面
function openPage(tabname, pageName, url) {
	if (0 < $("#" + pageName).length) {
		//在这里如果已经存在该页面则切换
		$("#main_tab #" + pageName + "_tab").click();
	} else {
		var tab = '<li>';
		tab += '<a id="' + pageName + '_tab" href="#' + pageName + '" data-toggle="tab" onclick="onMainTabSelect(this);">';
		tab += tabname
		tab += ' <span onclick="closeMainTab(this);"><span class="icon12 icon-remove"></span></span>';
		tab += '</a></li>';

		$("#main_tab").append(tab);
		$("#main_panel").append('<div class="tab-pane fade" id="' + pageName + '"><div id="sub_1"></div></div>');
		$("#" + pageName + " #sub_1").load(url);
		$("#main_tab #" + pageName + "_tab").click();
	}
}

//在当前页签打开一个页面
function openPageOnCurrentTab(url) {
	var element = $("#" + current_page);
	var id = "sub_" + (1 + element.children("div").size());
	element.append('<div id="' + id + '"></div>');
	element.children("div[id=" + id + "]").load(url);
	element.children("div").not("#" + id).hide();
	current_sub_page = id;
}

//当主页签被选中时调用
function onMainTabSelect(e) {
	var id = $(e).attr("id");
	current_tab = id;
	current_page = id.substring(0, id.indexOf("_tab"));
	current_sub_page = "sub_1";
}

//获得当前页面对象
function getCurrentPage() {
	return $("#" + current_page + " div[id=" + current_sub_page + "]");
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

//当WebSocket连接打开时调用
function onWSOpen() {
	//发送上线消息
	var obj = {};
	obj.ServiceName = "OnLine";
	obj.username = $("#login_username").text();
	wsClient.send(JSON.stringify(obj));
}

//当WebSocket连接关闭时调用
function onWSClose() {
}

//当WebSocket连接发生异常时调用
function onWSError(evt) {
}

//当服务器发来消息时调用
function onWSMessage(message) {
	messageService(message);
}

//向服务端发送消息
function sendMessage(message) {
	if ("string" != typeof message) {
		message = JSON.stringify(message);
	}

	wsClient.send(message);
}

//格式化时间
function formatTime(time, date) {
	if (!time || null == time || "" == time) {
		return "";
	}

	if (!date) {
		var now = new Date();
		date = now.getFullYear() + "-" + (1 + now.getMonth()) + "-" + (1 + now.getDate());
	}

	var date = new Date(date + " " + time);
	return date.Format("hh:mm");
}

//格式化日期
function formatDate(date) {
	if ("string" == typeof date) {
		date = new Date(date);
	}

	return date.Format("yyyy-MM-dd");
}

//格式化文本
function formatText(text) {
	switch (text) {
		case 'null':
		case null:
			return '';
			break;
		default:
			return text;
			break;
	}
}

//获得请求参数
function getRequestParam(name) { 
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); 
	var r = window.location.search.substr(1).match(reg); 
	
	if (r != null) 
		return unescape(r[2]); 
	return null; 
} 

//获得下拉框模式选项
function getSelectModelOption(model) {
	var option = '';

	switch (model) {
		case "ALL":
			option += '<option value="-1" selected="selected">全部</option>';
			break;
	}

	return option;
}

//当下拉框加载完毕时调用
function onSelectLoadComplete(name){}