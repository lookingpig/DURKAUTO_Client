$(document).ready(function() {
	//初始化菜单
    $("#menu").kendoMenu({
		select: onMenuSelect
	});
});
	
//当主菜单被选中时加载相应页面
function onMenuSelect(e) {
	var url = "";

	switch ($(e.item).children(".k-link").children().attr("name")) {
		case "appointment-inquiry" :
		url = "appointment/inquiry.html";
		break;
		case "appointment-define" :
		url = "appointment/define.html";
		break;
	}

	if ("" != url) {
		$("#main").load(url);
	}
}