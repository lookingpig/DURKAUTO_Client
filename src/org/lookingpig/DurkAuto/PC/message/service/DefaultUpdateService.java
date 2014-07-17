package org.lookingpig.DurkAuto.PC.message.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lookingpig.DurkAuto.PC.conf.ClientConfig;
import org.lookingpig.DurkAuto.PC.conf.StateCode;
import org.lookingpig.Tools.Database.DatabaseService;
import org.lookingpig.Tools.Service.MessageService.MessageService;
import org.lookingpig.Tools.Service.MessageService.Model.Message;

/**
 * 默认更新服务
 * @author Pig
 *
 */
public class DefaultUpdateService implements MessageService{
	private static final Logger logger = LogManager.getLogger(DefaultUpdateService.class);
	
	@Override
	public Message service(Message message) {
		logger.info("接收到一条更新消息：" + message);
		
		Message resMsg = new Message();
		DatabaseService service = DatabaseService.getService();
		
		if (null == service) {
			resMsg.addContent(StateCode.FLAG, StateCode.FALL_LOGIN_INPUTERROR);
			resMsg.addContent(StateCode.FALL_DATASERVICE_NOSTART, "数据服务没有正常启动！");
			return resMsg; 
		}
		
		boolean success = service.execute(message.getContent(ClientConfig.DATASERVICE_KEY_NAME), message.getContents());
		
		if (success) {
			resMsg.addContent(StateCode.FLAG, StateCode.SUCCESS);
		} else {
			resMsg.addContent(StateCode.FLAG, StateCode.FALL_UNKNOWN);
			logger.warn("执行更新消息失败！params: " + message.getContents());
		}
		
		return resMsg;
	}

}
