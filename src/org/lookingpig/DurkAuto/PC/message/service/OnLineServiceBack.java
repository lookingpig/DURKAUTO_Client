package org.lookingpig.DurkAuto.PC.message.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lookingpig.DurkAuto.PC.client.WSClient;
import org.lookingpig.Tools.Service.MessageService.MessageService;
import org.lookingpig.Tools.Service.MessageService.Model.Message;

/**
 * 在服务器上线响应处理
 * @author Pig
 *
 */
public class OnLineServiceBack implements MessageService {
	private static final Logger logger = LogManager.getLogger(OnLineServiceBack.class);
	
	@Override
	public Message service(Message message) {
		WSClient client = (WSClient) message.getCaller();
		String state = message.getContent("state");
		
		if (null != state && "success".equals(state)) {
			client.setIdle(true);
			WSClient.addClient(client);
			logger.info("在服务器上线成功。");
		} else {
			client.setIdle(false);
			client.close();
			logger.warn("在服务器上线失败！");
		}
		
		return null;
	}

}
