package org.lookingpig.DurkAuto.PC.message.service.appointment;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lookingpig.DurkAuto.PC.conf.ClientConfig;
import org.lookingpig.DurkAuto.PC.conf.StateCode;
import org.lookingpig.Tools.Database.DatabaseService;
import org.lookingpig.Tools.Service.MessageService.MessageService;
import org.lookingpig.Tools.Service.MessageService.Model.Message;

/**
 * 添加预约服务
 * @author Pig
 *
 */
public class AddAppointService implements MessageService {
	private static final Logger logger = LogManager.getLogger(AddAppointService.class);
	
	@Override
	public Message service(Message message) {
		logger.info("接收到一条预约服务-新增预约消息：" + message);
		
		Message resMsg = new Message();
		DatabaseService service = DatabaseService.getService();
		
		if (null == service) {
			resMsg.addContent(StateCode.FLAG, StateCode.FALL_LOGIN_INPUTERROR);
			resMsg.addContent(StateCode.FALL_DATASERVICE_NOSTART, "数据服务没有正常启动！");
			return resMsg; 
		}
		
		Map<String, String> parames = new HashMap<String, String>();
		
		//获取类型信息
		parames.put("typeID", message.getContent("serviceType"));
		List<List<String>> result = service.query("Appointment_GetSpecialServiceType", parames);
		
		if (0 >= result.size()) {
			//类型不存在
			logger.warn("新增预约失败！预约类型不存在，类型： " + parames.get("typeID"));
			resMsg.addContent(StateCode.FLAG, StateCode.FALL_APPOINTMENT_SERVICETYPE_UNDEFINE);
		} else if ("0".equals(result.get(1).get(11))) {
			//类型未启用
			logger.warn("新增预约失败！预约类型未启用，类型： " + result.get(0).get(1));
			resMsg.addContent(StateCode.FLAG, StateCode.FALL_APPOINTMENT_SERVICETYPE_DISABLE);
		} else {
			//计算提醒时间
			String timeBasis = result.get(1).get(9);
			long reminder = Long.parseLong(result.get(1).get(7));
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ClientConfig.TIME_FORMAT);
			LocalTime reminderTime = LocalTime.parse(message.getContent("appointTime")); 
			
			switch (timeBasis) {
			case "h":
				reminderTime = reminderTime.minus(reminder, ChronoUnit.HOURS);
				break;
			case "m":
				reminderTime = reminderTime.minus(reminder, ChronoUnit.MINUTES);
				break;
			}
			
			//插入数据
			parames.clear();
			parames.put("serviceType", message.getContent("serviceType"));
			parames.put("appointTime", message.getContent("appointTime"));
			parames.put("reminderTime", formatter.format(reminderTime));
			
			boolean success = service.execute("Appointment_AddAppoint", parames);
			
			if (success) {
				resMsg.addContent(StateCode.FLAG, StateCode.SUCCESS);
			} else {
				resMsg.addContent(StateCode.FLAG, StateCode.FALL_APPOINTMENT_ADD);
				logger.warn("新增预约失败！参数: " + parames);
			}
		}
		
		return resMsg;
	}

}
