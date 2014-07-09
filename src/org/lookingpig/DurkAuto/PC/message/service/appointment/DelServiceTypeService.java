package org.lookingpig.DurkAuto.PC.message.service.appointment;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lookingpig.DurkAuto.PC.conf.StateCode;
import org.lookingpig.Tools.Database.DatabaseService;
import org.lookingpig.Tools.Service.MessageService.MessageService;
import org.lookingpig.Tools.Service.MessageService.Model.Message;

/**
 * 预约服务-删除服务类型服务
 * @author Pig
 *
 */
public class DelServiceTypeService implements MessageService {
	private static final Logger logger = LogManager.getLogger(DelServiceTypeService.class);
	
	@Override
	public Message service(Message message) {
		logger.info("接收到一条预约服务-删除服务类型消息：" + message);
		
		Message resMsg = new Message();
		DatabaseService service = DatabaseService.getService();
		
		if (null == service) {
			resMsg.addContent(StateCode.FLAG, StateCode.FALL_LOGIN_INPUTERROR);
			resMsg.addContent(StateCode.FALL_DATASERVICE_NOSTART, "数据服务没有正常启动！");
			return resMsg; 
		}
		
		Map<String, String> parames = new HashMap<String, String>();
		parames.put("id", message.getContent("id"));
		
		boolean success = service.execute("Appointment_DelServiceType", parames);
		
		if (success) {
			resMsg.addContent(StateCode.FLAG, StateCode.SUCCESS);
		} else {
			resMsg.addContent(StateCode.FLAG, StateCode.FALL_APPOINTMENT_SERVICETYPE_DEL);
			logger.warn("删除预约服务类型失败！参数: " + parames);
		}
		
		return resMsg;
	}

}
