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
 * 预约服务-新增服务类型服务
 * @author Pig
 *
 */
public class AddServiceTypeService implements MessageService {
	private static final Logger logger = LogManager.getLogger(AddServiceTypeService.class);
	
	@Override
	public Message service(Message message) {
		logger.info("接收到一条预约服务-新增服务类型消息：" + message);
		
		Message resMsg = new Message();
		DatabaseService service = DatabaseService.getService();
		
		if (null == service) {
			resMsg.addContent(StateCode.FLAG, StateCode.FALL_LOGIN_INPUTERROR);
			resMsg.addContent(StateCode.FALL_DATASERVICE_NOSTART, "数据服务没有正常启动！");
			return resMsg; 
		}
		
		Map<String, String> parames = new HashMap<String, String>();
		parames.put("typeName", message.getContent("typeName"));
		parames.put("parking", message.getContent("parking"));
		parames.put("businessDate", message.getContent("businessDate"));
		parames.put("businessHoursStart", message.getContent("businessHoursStart"));
		parames.put("businessHoursEnd", message.getContent("businessHoursEnd"));
		parames.put("serviceTime", message.getContent("serviceTime"));
		parames.put("reminderTime", message.getContent("reminderTime"));
		parames.put("waitTime", message.getContent("waitTime"));
		parames.put("timeBasis", message.getContent("timeBasis"));
		parames.put("exclusive", message.getContent("exclusive"));
		parames.put("enable", message.getContent("enable"));
		
		boolean success = service.execute("AddAppointmentServiceType", parames);
		
		if (success) {
			resMsg.addContent(StateCode.FLAG, StateCode.SUCCESS);
		} else {
			resMsg.addContent(StateCode.FLAG, StateCode.FALL_APPOINTMENT_SERVICETYPE_ADD);
			logger.warn("新增预约服务类型失败！参数: " + parames);
		}
		
		return resMsg;
	}

}




