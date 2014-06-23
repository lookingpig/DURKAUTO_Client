package org.lookingpig.DurkAuto.PC.message.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lookingpig.DurkAuto.PC.conf.StateCode;
import org.lookingpig.Tools.Database.DatabaseService;
import org.lookingpig.Tools.Service.MessageService.MessageService;
import org.lookingpig.Tools.Service.MessageService.Model.Message;

/**
 * 登陆服务
 * @author Pig
 *
 */
public class LoginService implements MessageService {
	private static final Logger logger = LogManager.getLogger(LoginService.class);
	
	@Override
	public Message service(Message message) {
		logger.info("接收到一条登陆消息：" + message);
		
		Message resMsg = new Message();
		DatabaseService service = DatabaseService.getService();
		
		if (null == service) {
			resMsg.addContent(StateCode.FLAG, StateCode.FALL_LOGIN_INPUTERROR);
			resMsg.addContent(StateCode.FALL_DATASERVICE_NOSTART, "数据服务没有正常启动！");
			return resMsg; 
		}
		
		List<List<String>> result = null;
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", message.getContent("username"));
		params.put("password", message.getContent("password"));
		
		result = service.query("GetUserInfo", params);
		
		if (1 == result.size()) {
			resMsg.addContent(StateCode.FLAG, StateCode.SUCCESS);
			resMsg.addContent("id", result.get(0).get(0));
			resMsg.addContent("username", result.get(0).get(1));
			resMsg.addContent("email", result.get(0).get(4));
			resMsg.addContent("phone", result.get(0).get(5));
			resMsg.addContent("realname", result.get(0).get(6));
		} else {
			resMsg.addContent(StateCode.FLAG, StateCode.FALL_LOGIN_INPUTERROR);
			resMsg.addContent(StateCode.DESCRIBE_FLAG, "用户名或密码错误！");
		}
		
		return resMsg;
	}
}
