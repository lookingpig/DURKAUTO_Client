package org.lookingpig.DurkAuto.PC.message.service;

import javax.servlet.http.HttpSession;

import org.lookingpig.DurkAuto.PC.conf.StateCode;
import org.lookingpig.Tools.Service.MessageService.MessageService;
import org.lookingpig.Tools.Service.MessageService.Model.Message;

/**
 * 注销服务
 * @author Pig
 *
 */
public class LogoutService implements MessageService {

	@Override
	public Message service(Message message) {
		HttpSession session = (HttpSession) message.getSession();
		session.invalidate();
		
		Message resMsg = new Message();
		resMsg.addContent(StateCode.FLAG, StateCode.SUCCESS);
		return resMsg;
	}

}
