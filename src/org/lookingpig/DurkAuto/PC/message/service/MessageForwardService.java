package org.lookingpig.DurkAuto.PC.message.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lookingpig.DurkAuto.PC.client.WSClient;
import org.lookingpig.DurkAuto.PC.conf.ClientConfig;
import org.lookingpig.Tools.Service.MessageService.MessageService;
import org.lookingpig.Tools.Service.MessageService.Model.Message;

/**
 * 消息转发服务
 * @author Pig
 *
 */
public class MessageForwardService implements MessageService {
	private static final Logger logger = LogManager.getLogger(MessageForwardService.class);
	
	@Override
	public Message service(Message msg) {		
		
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ClientConfig.DATETIME_FORMAT);
			LocalDateTime now = LocalDateTime.now();
			msg.setSendTime(formatter.format(now));
			
			WSClient.getClient().sendMessage(msg);
		} catch (IOException e) {
			logger.error("向服务器发送消息失败！", e);
		} catch (Exception e) {
			logger.error("消息转发服务执行异常！", e);
		}
		return null;
	}

}
