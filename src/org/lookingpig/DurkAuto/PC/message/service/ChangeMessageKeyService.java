package org.lookingpig.DurkAuto.PC.message.service;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lookingpig.DurkAuto.PC.client.WSClient;
import org.lookingpig.DurkAuto.PC.conf.ClientConfig;
import org.lookingpig.Tools.Service.MessageService.MessageService;
import org.lookingpig.Tools.Service.MessageService.Model.Message;

/**
 * 更换消息通讯密匙服务
 * @author Pig
 *
 */
public class ChangeMessageKeyService implements MessageService {
	private static final Logger logger = LogManager.getLogger(ChangeMessageKeyService.class);
	@Override
	public Message service(Message message) {
		WSClient client = (WSClient) message.getCaller();
		client.setAESKey(message.getContent("AESKey"));
		logger.info("更换通讯密匙成功，新密匙为：" + message.getContent("AESKey"));
		
		// 发送上线消息
		logger.info("向服务器发送上线消息");
		Message msg = new Message();
		msg.addContent(ClientConfig.MESSAGESERVICE_KEY_NAME, "OnLineService");

		try {
			client.sendLocalMessage(msg);
		} catch (IOException e) {
			logger.error("向服务器发送上线消息失败，原因：", e);
		}
		return null;
	}

}
