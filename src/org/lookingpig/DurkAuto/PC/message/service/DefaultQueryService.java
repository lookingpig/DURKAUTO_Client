package org.lookingpig.DurkAuto.PC.message.service;

import java.util.List;

import net.sf.json.JSONArray;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lookingpig.DurkAuto.PC.conf.ClientConfig;
import org.lookingpig.DurkAuto.PC.conf.StateCode;
import org.lookingpig.DurkAuto.PC.service.Service;
import org.lookingpig.Tools.Database.DatabaseService;
import org.lookingpig.Tools.Service.MessageService.MessageService;
import org.lookingpig.Tools.Service.MessageService.Model.Message;

/**
 * 默认查询服务
 * @author Pig
 *
 */
public class DefaultQueryService implements MessageService {
	private static final Logger logger = LogManager.getLogger(DefaultQueryService.class);
	
	@Override
	public Message service(Message message) {
		logger.info("接收到一条查询消息：" + message);
		
		Message resMsg = new Message();
		DatabaseService service = DatabaseService.getService();
		
		if (null == service) {
			resMsg.addContent(StateCode.FLAG, StateCode.FALL_LOGIN_INPUTERROR);
			resMsg.addContent(StateCode.FALL_DATASERVICE_NOSTART, "数据服务没有正常启动！");
			return resMsg; 
		}
		
		List<List<String>> result = service.query(message.getContent(ClientConfig.DATASERVICE_KEY_NAME), message.getContents());
		resMsg.addContent(StateCode.FLAG, StateCode.SUCCESS);
		
		if (0 < result.size()) {
			JSONArray resRow = Service.baleDataToJSONArray(result);
			resMsg.addContent("Data", resRow.toString());
		}
		
		return resMsg;
	}

}
