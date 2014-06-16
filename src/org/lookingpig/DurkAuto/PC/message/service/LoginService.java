package org.lookingpig.DurkAuto.PC.message.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@Override
	public Message service(Message message) {
		DatabaseService data = DatabaseService.getService();
		List<List<String>> result = null;
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", message.getContent("username"));
		params.put("password", message.getContent("password"));
		
		result = data.query("GetUserInfo", params);
		Message resMsg = new Message();
		
		if (1 == result.size()) {
			resMsg.addContent(StateCode.FLAG, StateCode.SUCCESS);
			resMsg.addContent("id", result.get(0).get(1));
			resMsg.addContent("username", result.get(0).get(2));
			resMsg.addContent("email", result.get(0).get(5));
			resMsg.addContent("phone", result.get(0).get(6));
			resMsg.addContent("realname", result.get(0).get(7));
		} else {
			resMsg.addContent(StateCode.FLAG, StateCode.FALL_LOGIN_INPUTERROR);
			resMsg.addContent(StateCode.DESCRIBE_FLAG, "用户名或密码错误！");
		}
		
		return resMsg;
	}
}
