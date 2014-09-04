package org.lookingpig.DurkAuto.PC.message.service.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lookingpig.DurkAuto.PC.conf.StateCode;
import org.lookingpig.DurkAuto.PC.service.Service;
import org.lookingpig.Tools.Database.DatabaseService;
import org.lookingpig.Tools.Service.MessageService.MessageService;
import org.lookingpig.Tools.Service.MessageService.Model.Message;

/**
 * 初始化服务
 * @author Pig
 *
 */
public class InitializeService implements MessageService {
	private static final Logger logger = LogManager.getLogger(InitializeService.class);
	
	@Override
	public Message service(Message message) {
		logger.info("接收到一条获取初始化信息消息");
		
		Message resMsg = new Message();
		DatabaseService service = DatabaseService.getService();
		
		if (null == service) {
			resMsg.addContent(StateCode.FLAG, StateCode.FALL_LOGIN_INPUTERROR);
			resMsg.addContent(StateCode.FALL_DATASERVICE_NOSTART, "数据服务没有正常启动！");
			return resMsg; 
		}
		
		Map<String, String> parames = new HashMap<String, String>();
		//获取可用服务类型信息
		parames.put("enable", String.valueOf(true));
		List<List<String>> result = service.query("Appointment_GetServiceType", parames);
		
		resMsg.addContent(StateCode.FLAG, StateCode.SUCCESS);
		return resMsg;
	}
}
