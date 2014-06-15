package org.lookingpig.DurkAuto.PC.message.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.lookingpig.DurkAuto.PC.conf.ClientConfig;
import org.lookingpig.DurkAuto.PC.service.WSService;
import org.lookingpig.Tools.Service.MessageService.MessageService;
import org.lookingpig.Tools.Service.MessageService.Model.Message;

/**
 * 用户上线服务
 * @author Pig
 *
 */
public class OnLineService implements MessageService {

	@Override
	public Message service(Message message) {
		WSService service = (WSService) message.getCaller();
		service.setName(message.getSender());
		WSService.addClient(service.getName(), service);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ClientConfig.DATETIME_FORMAT);
		LocalDateTime now = LocalDateTime.now();
		
		//回应一条成功消息
		Message response = new Message();
		response.setSender(ClientConfig.getConfig("durkauto.pc.sender"));
		response.setSendNumber(ClientConfig.getConfig("durkauto.pc.sendnumber"));
		response.setSendTime(formatter.format(now));
		response.addContent(ClientConfig.MESSAGESERVICE_KEY_NAME, message.getContent(ClientConfig.MESSAGESERVICE_KEY_NAME));
		response.addContent(ClientConfig.MESSAGESERVICE_TYPE, ClientConfig.MESSAGESERVICE_TYPE_RESPONSE);
		response.addContent("state", "success");
		return response;
	}

}
