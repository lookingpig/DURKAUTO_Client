<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" >
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta name="author" content="Dean Zhu" />
		<meta name="application-name" content="DURKAUTO" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />

    	<title>DURKAUTO</title>

    	<!-- 加载模板使用的样式文件 -->
    	<link href="css/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css" />
	    <link href="css/bootstrap/bootstrap-responsive.min.css" rel="stylesheet" type="text/css" />
	    <link href="css/supr-theme/jquery.ui.supr.css" rel="stylesheet" type="text/css"/>
	    <link href="css/icons.css" rel="stylesheet" type="text/css" />
	    
	    <!-- Plugin stylesheets -->
	    <link href="plugins/qtip/jquery.qtip.css" rel="stylesheet" type="text/css" />
	    <link href="plugins/fullcalendar/fullcalendar.css" rel="stylesheet" type="text/css" />
	    <link href="plugins/jpages/jPages.css" rel="stylesheet" type="text/css" />
	    <link href="plugins/prettify/prettify.css" type="text/css" rel="stylesheet" />
	    <link href="plugins/inputlimiter/jquery.inputlimiter.css" type="text/css" rel="stylesheet" />
	    <link href="plugins/ibutton/jquery.ibutton.css" type="text/css" rel="stylesheet" />
	    <link href="plugins/uniform/uniform.default.css" type="text/css" rel="stylesheet" />
	    <link href="plugins/color-picker/color-picker.css" type="text/css" rel="stylesheet" />
	    <link href="plugins/select/select2.css" type="text/css" rel="stylesheet" />
	    <link href="plugins/validate/validate.css" type="text/css" rel="stylesheet" />
	    <link href="plugins/pnotify/jquery.pnotify.default.css" type="text/css" rel="stylesheet" />
	    <link href="plugins/pretty-photo/prettyPhoto.css" type="text/css" rel="stylesheet" />
	    <link href="plugins/smartWizzard/smart_wizard.css" type="text/css" rel="stylesheet" />
	    <link href="plugins/dataTables/jquery.dataTables.css" type="text/css" rel="stylesheet" />
	    <link href="plugins/elfinder/elfinder.css" type="text/css" rel="stylesheet" />
	    <link href="plugins/plupload/jquery.ui.plupload/css/jquery.ui.plupload.css" type="text/css" rel="stylesheet" />
	    <link href="plugins/search/tipuesearch.css" type="text/css" rel="stylesheet" />

	    <!-- Main stylesheets -->
	    <link href="css/main.css" rel="stylesheet" type="text/css" /> 
	    <!-- Right to left version 
	    <link href="css/rtl.css" rel="stylesheet" type="text/css" /> -->

	    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
	    <!--[if lt IE 9]>
	      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
	    <![endif]-->

	    <!-- Le fav and touch icons -->
	    <link rel="shortcut icon" href="images/favicon.ico" />
	    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="images/apple-touch-icon-144-precomposed.png" />
	    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="images/apple-touch-icon-114-precomposed.png" />
	    <link rel="apple-touch-icon-precomposed" sizes="72x72" href="images/apple-touch-icon-72-precomposed.png" />
	    <link rel="apple-touch-icon-precomposed" href="images/apple-touch-icon-57-precomposed.png" />
	    
	    <script type="text/javascript">
	        //adding load class to body and hide page
	        document.documentElement.className += 'loadstate';
	    </script>
	    <!-- 加载模板使用的样式文件结束 -->
	</head>

	<body>
		<div id="qLoverlay"></div>
    	<div id="qLbar"></div>
    	
		<!-- 标题栏 -->
		<div id="header">

	        <div class="navbar">
	            <div class="navbar-inner">
	              <div class="container-fluid">
	              	<!-- Logo -->
	                <a class="brand" href="dashboard.html">
	                	<img src="images/logo.jpg" alt="logo" width="90px" height="auto">
	                </a>

	                <div class="nav-no-collapse">

	                	<!-- 快捷方式 -->
	                    <ul class="nav">
	                    	<!-- 操作台 -->
	                        <li class="active">
	                        	<a href="dashboard.html">
	                        		<span class="icon16 icomoon-icon-screen-2"></span> 
	                        		Dashboard
	                        	</a>
	                        </li>

	                        <!-- 设置 -->
	                        <li class="dropdown">
	                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
	                                <span class="icon16 icomoon-icon-cog"></span> Settings
	                                <b class="caret"></b>
	                            </a>
	                            <ul class="dropdown-menu">
	                                <li class="menu">
	                                    <ul>
	                                        <li>                                                    
	                                            <a href="#"><span class="icon16 icomoon-icon-equalizer"></span>Site config</a>
	                                        </li>
	                                        <li>                                                    
	                                            <a href="#"><span class="icon16 icomoon-icon-wrench"></span>Plugins</a>
	                                        </li>
	                                        <li>
	                                            <a href="#"><span class="icon16 icomoon-icon-picture-2"></span>Themes</a>
	                                        </li>
	                                    </ul>
	                                </li>
	                            </ul>
	                        </li>

	                        <!-- 消息 -->
	                        <li class="dropdown">
	                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
	                                <span class="icon16 icomoon-icon-mail-3"></span>Messages 
	                                <span class="notification">0</span>
	                            </a>
	                            <ul class="dropdown-menu">
	                                <li class="menu">
	                                    <ul class="messages">    
	                                        <li class="header"><strong>Messages</strong> (10) emails and (2) PM</li>
	                                        <li>
	                                           <span class="icon"><span class="icon16 icomoon-icon-user-3"></span></span>
	                                            <span class="name"><a data-toggle="modal" href="#myModal1"><strong>Sammy Morerira</strong></a><span class="time">35 min ago</span></span>
	                                            <span class="msg">I have question about new function ...</span>
	                                        </li>
	                                        <li>
	                                           <span class="icon avatar"><img src="images/avatar.jpg" alt="" /></span>
	                                            <span class="name"><a data-toggle="modal" href="#myModal1"><strong>George Michael</strong></a><span class="time">1 hour ago</span></span>
	                                            <span class="msg">I need to meet you urgent please call me ...</span>
	                                        </li>
	                                        <li>
	                                            <span class="icon"><span class="icon16 icomoon-icon-mail-3"></span></span>
	                                            <span class="name"><a data-toggle="modal" href="#myModal1"><strong>Ivanovich</strong></a><span class="time">1 day ago</span></span>
	                                            <span class="msg">I send you my suggestion, please look and ...</span>
	                                        </li>
	                                        <li class="view-all"><a href="#">View all messages <span class="icon16 icomoon-icon-arrow-right-8"></span></a></li>
	                                    </ul>
	                                </li>
	                            </ul>
	                        </li>
	                    </ul>
	                  
	                  	<!-- 登陆 -->
	                    <ul class="nav pull-right usernav">

	                    	<!-- 通知 -->
	                        <li class="dropdown">
	                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
	                                <span class="icon16 icomoon-icon-bell-2"></span>
	                                <span class="notification">0</span>
	                            </a>

	                            <ul class="dropdown-menu">
	                                <li class="menu">
	                                    <ul class="notif">
	                                        <li class="header"><strong>Notifications</strong> (3) items</li>
	                                        <li>
	                                            <a href="#">
	                                                <span class="icon"><span class="icon16 icomoon-icon-user-3"></span></span>
	                                                <span class="event">1 User is registred</span>
	                                            </a>
	                                        </li>
	                                        <li>
	                                            <a href="#">
	                                                <span class="icon"><span class="icon16 icomoon-icon-comments-4"></span></span>
	                                                <span class="event">Jony add 1 comment</span>
	                                            </a>
	                                        </li>
	                                        <li>
	                                            <a href="#">
	                                                <span class="icon"><span class="icon16 icomoon-icon-new-2"></span></span>
	                                                <span class="event">admin Julia added post with a long description</span>
	                                            </a>
	                                        </li>
	                                        <li class="view-all"><a href="#">View all notifications <span class="icon16 icomoon-icon-arrow-right-8"></span></a></li>
	                                    </ul>
	                                </li>
	                            </ul>
	                        </li>

	                        <!-- 用户 -->
	                        <li class="dropdown">
	                            <a href="#" class="dropdown-toggle avatar" data-toggle="dropdown">
	                                <img src="images/avatar.jpg" alt="" class="image" /> 
	                                <span class="txt"id="login_username">${username}</span>
	                                <b class="caret"></b>
	                            </a>
	                            <ul class="dropdown-menu">
	                                <li class="menu">
	                                    <ul>
	                                        <li>
	                                            <a href="#"><span class="icon16 icomoon-icon-user-3"></span>Edit profile</a>
	                                        </li>
	                                        <li>
	                                            <a href="#"><span class="icon16 icomoon-icon-comments-2"></span>Approve comments</a>
	                                        </li>
	                                        <li>
	                                            <a href="#"><span class="icon16 icomoon-icon-plus-2"></span>Add user</a>
	                                        </li>
	                                    </ul>
	                                </li>
	                            </ul>
	                        </li>

	                        <!-- 退出 -->
	                        <li>
	                        	<a href="#" onclick="onLogout();">
	                        		<span class="icon16 icomoon-icon-exit"></span> 退出
	                        	</a>
	                        </li>
	                    </ul>
	                </div><!-- /.nav-collapse -->
	              </div>
	            </div><!-- /navbar-inner -->
	          </div><!-- /navbar --> 

	    </div><!-- End #header -->

	    <!-- 导航栏 -->
	    <div id="sidebar">
	    	<!-- 快捷栏 -->
    		<div class="shortcuts">
                <ul>
                    <li>
                    	<a href="support.html" title="Support section" class="tip"><span class="icon24 icomoon-icon-support"></span></a>
                    </li>
                    <li>
                    	<a href="#" title="Database backup" class="tip"><span class="icon24 icomoon-icon-database"></span></a>
                    </li>
                    <li>
                    	<a href="charts.html" title="Sales statistics" class="tip"><span class="icon24 icomoon-icon-pie-2"></span></a>
                    </li>
                    <li>
                    	<a href="#" title="Write post" class="tip"><span class="icon24 icomoon-icon-pencil"></span></a>
                    </li>
                </ul>
            </div><!-- End search -->  

            <!-- 菜单 -->
            <div class="sidenav">

                <div class="sidebar-widget" style="margin: -1px 0 0 0;">
                    <h5 class="title" style="margin-bottom:0">导航栏</h5>
                </div><!-- End .sidenav-widget -->

                <div class="mainnav">
                    <ul style="width: 210px">
                        <li>
                            <a href="#">
                            	<span class="icon16 icomoon-icon-list-view-2"></span>
                            	预约服务
                            </a>
                            <ul class="sub" style="width: 210px">
                                <li>
                                	<a href="#" onclick="onMainMenuSelect(this);" name="appointment-inquiry">
                                		<span class="icon16 icomoon-icon-arrow-right-2"></span>
                                		服务查询
                                	</a>
                                </li>
                                <li>
                                	<a href="#" onclick="onMainMenuSelect(this);" name="appointment-define">
                                		<span class="icon16 icomoon-icon-arrow-right-2"></span>
                                		服务类型
                                	</a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </div><!-- End sidenav -->

    	</div>

    	<!-- 主界面 -->
	    <div id="wrapper">
	    	<!--Responsive navigation button-->  
	    	<div class="resBtn">
	            <a href="#"><span class="icon16 minia-icon-list-3"></span></a>
	        </div>

	        <!-- 隐藏按钮 -->  
	        <div class="collapseBtn leftbar">
	            <a href="#" class="tipR" title="Hide Left Sidebar">
	            	<span class="icon12 minia-icon-layout"></span>
	            </a>
	        </div>

	        <!--Sidebar background-->
        	<div id="sidebarbg"></div>
        	
        	<div id="content" class="clearfix">
        		<div class="contentwrapper">

        			<!-- 标题栏 -->
        			<div class="heading">

	                    <h3>Dashboard</h3>                    

	                    <div class="resBtnSearch">
	                        <a href="#"><span class="icon16 icomoon-icon-search-3"></span></a>
	                    </div>

	                    <div class="search">

	                        <form id="searchform" action="search.html">
	                            <input type="text" id="tipue_search_input" class="top-search" placeholder="Search here ..." />
	                            <input type="submit" id="tipue_search_button" class="search-btn" value=""/>
	                        </form>
	                
	                    </div><!-- End search -->
	                    
	                    <ul class="breadcrumb">
	                        <li>You are here:</li>
	                        <li>
	                            <a href="#" class="tip" title="back to dashboard">
	                                <span class="icon16 icomoon-icon-screen-2"></span>
	                            </a> 
	                            <span class="divider">
	                                <span class="icon16 icomoon-icon-arrow-right-2"></span>
	                            </span>
	                        </li>
	                        <li class="active">Dashboard</li>
	                    </ul>

	                </div><!-- End .heading-->

                	<div class="row-fluid">
    
                        <div style="margin-bottom: 20px;">
                            <ul id="main_tab" class="nav nav-tabs pattern">
                                <li class="active">
                                	<a href="#home" data-toggle="tab">Home</a>
                                </li>
                                <li><a href="#profile" data-toggle="tab">Profile</a></li>
                            </ul>

                            <div class="tab-content" id="main_panel">
                                <div class="tab-pane fade in active" id="home">
                                    <p>Raw denim you probably haven't heard of them jean shorts Austin. </p>
                                </div>
                                <div class="tab-pane fade" id="profile">
                                    <p>Food truck fixie locavore, accusamus mcsweeney's marfa nulla single-origin coffee squid. </p>
                                </div>
                            </div>
                        </div>

                    </div><!-- End .row-fluid -->
	                
        		</div>
        	</div>
	    </div>

	    <!-- 加载模板使用的脚本 -->
		<!-- Le javascript
	    ================================================== -->
	    <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
	    <script type="text/javascript" src="js/bootstrap/bootstrap.js"></script>  
	    <script type="text/javascript" src="js/jquery.cookie.js"></script>
	    <script type="text/javascript" src="js/jquery.mousewheel.js"></script>

	    <!-- Load plugins -->
	    <script type="text/javascript" src="plugins/qtip/jquery.qtip.min.js"></script>
	    <script type="text/javascript" src="plugins/flot/jquery.flot.js"></script>
	    <script type="text/javascript" src="plugins/flot/jquery.flot.grow.js"></script>
	    <script type="text/javascript" src="plugins/flot/jquery.flot.pie.js"></script>
	    <script type="text/javascript" src="plugins/flot/jquery.flot.resize.js"></script>
	    <script type="text/javascript" src="plugins/flot/jquery.flot.tooltip_0.4.4.js"></script>
	    <script type="text/javascript" src="plugins/flot/jquery.flot.orderBars.js"></script>

	    <script type="text/javascript" src="plugins/sparkline/jquery.sparkline.min.js"></script>
	    <script type="text/javascript" src="plugins/knob/jquery.knob.js"></script>
	    <script type="text/javascript" src="plugins/fullcalendar/fullcalendar.min.js"></script>
	    <script type="text/javascript" src="plugins/prettify/prettify.js"></script>

	    <script type="text/javascript" src="plugins/watermark/jquery.watermark.min.js"></script>
	    <script type="text/javascript" src="plugins/elastic/jquery.elastic.js"></script>
	    <script type="text/javascript" src="plugins/inputlimiter/jquery.inputlimiter.1.3.min.js"></script>
	    <script type="text/javascript" src="plugins/maskedinput/jquery.maskedinput-1.3.min.js"></script>
	    <script type="text/javascript" src="plugins/ibutton/jquery.ibutton.min.js"></script>
	    <script type="text/javascript" src="plugins/uniform/jquery.uniform.min.js"></script>
	    <script type="text/javascript" src="plugins/stepper/ui.stepper.js"></script>
	    <script type="text/javascript" src="plugins/color-picker/colorpicker.js"></script>
	    <script type="text/javascript" src="plugins/timeentry/jquery.timeentry.min.js"></script>
	    <script type="text/javascript" src="plugins/select/select2.min.js"></script>
	    <script type="text/javascript" src="plugins/dualselect/jquery.dualListBox-1.3.min.js"></script>
	    <script type="text/javascript" src="plugins/tiny_mce/jquery.tinymce.js"></script>
	    <script type="text/javascript" src="plugins/validate/jquery.validate.min.js"></script>
	    <script type="text/javascript" src="plugins/search/tipuesearch_set.js"></script>
	    <script type="text/javascript" src="plugins/search/tipuesearch_data.js"></script><!-- JSON for searched results -->
	    <script type="text/javascript" src="plugins/search/tipuesearch.js"></script>

	    <script type="text/javascript" src="plugins/animated-progress-bar/jquery.progressbar.js"></script>
	    <script type="text/javascript" src="plugins/pnotify/jquery.pnotify.min.js"></script>
	    <script type="text/javascript" src="plugins/lazy-load/jquery.lazyload.min.js"></script>
	    <script type="text/javascript" src="plugins/jpages/jPages.min.js"></script>
	    <script type="text/javascript" src="plugins/pretty-photo/jquery.prettyPhoto.js"></script>
	    <script type="text/javascript" src="plugins/smartWizzard/jquery.smartWizard-2.0.min.js"></script>

	    <script type="text/javascript" src="plugins/ios-fix/ios-orientationchange-fix.js"></script>

	    <script type="text/javascript" src="plugins/dataTables/jquery.dataTables.min.js"></script>
	    <script type="text/javascript" src="plugins/elfinder/elfinder.min.js"></script>
	    <script type="text/javascript" src="plugins/plupload/plupload.js"></script>
	    <script type="text/javascript" src="plugins/plupload/plupload.html4.js"></script>
	    <script type="text/javascript" src="plugins/plupload/jquery.plupload.queue/jquery.plupload.queue.js"></script>
	    <script type="text/javascript" src="plugins/totop/jquery.ui.totop.min.js"></script> 

	    <!-- Init plugins -->
	    <script type="text/javascript" src="js/statistic.js"></script><!-- Control graphs ( chart, pies and etc) -->

	    <!-- Important Place before main.js  -->
	    <script type="text/javascript" src="js/supr-theme/jquery-ui-1.8.21.custom.min.js"></script>
	    <script type="text/javascript" src="plugins/touch-punch/jquery.ui.touch-punch.min.js"></script>
	    <script type="text/javascript" src="js/main.js"></script>
	    <!-- 加载模板使用的脚本结束 -->

	    <!-- 加载需要使用的脚本 -->
	    <script type="text/javascript" src="js/ws.client.js"></script>
	    <script type="text/javascript" src="js/durkauto.messageservice.js"></script>
	    <script type="text/javascript" src="js/durkauto.date.js"></script>
	    <script type="text/javascript" src="js/durkauto.main.js"></script>
	</body>
</html>