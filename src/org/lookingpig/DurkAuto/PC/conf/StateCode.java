package org.lookingpig.DurkAuto.PC.conf;

/**
 * 错误定义
 * @author Pig
 *
 */
public class StateCode {
	public static final String FLAG = "StateCode";
	public static final String DESCRIBE_FLAG = "StateDesc";
	
	public static final String SUCCESS = "000000000";
	
	public static final String FALL_UNKNOWN = "100000000";							//未知错误
	public static final String FALL_DATASERVICE_NOSTART = "101001000";				//数据服务-服务未启动
	public static final String FALL_MESSAGESERVICE_NOTFOUND = "102001000";			//消息服务-服务未找到
	
	public static final String FALL_LOGIN_INPUTERROR = "100001000";				//登陆-用户名或密码错误
	
	public static final String FALL_APPOINTMENT_SERVICETYPE_ADD = "103001000";		//预约服务-服务类型-新增
	
	
}
