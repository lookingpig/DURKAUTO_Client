package org.lookingpig.DurkAuto.PC.message.service.appointment;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lookingpig.DurkAuto.PC.conf.StateCode;
import org.lookingpig.Tools.Database.DatabaseService;
import org.lookingpig.Tools.Service.MessageService.MessageService;
import org.lookingpig.Tools.Service.MessageService.Model.Message;

/**
 * 预约服务-查询服务类型服务
 * @author Pig
 *
 */
public class QueryServiceTypeService implements MessageService {
	private static final Logger logger = LogManager.getLogger(QueryServiceTypeService.class);
	
	@Override
	public Message service(Message message) {
		logger.info("接收到一条预约服务-查询服务类型消息：" + message);
		
		Message resMsg = new Message();
		DatabaseService service = DatabaseService.getService();
		
		if (null == service) {
			resMsg.addContent(StateCode.FLAG, StateCode.FALL_LOGIN_INPUTERROR);
			resMsg.addContent(StateCode.FALL_DATASERVICE_NOSTART, "数据服务没有正常启动！");
			return resMsg; 
		}
		
		List<List<String>> result = null;
		result = service.query("GetAppointmentServiceType", null);
		resMsg.addContent(StateCode.FLAG, StateCode.SUCCESS);
		
		if (0 < result.size()) {
			//将返回数据打包成JSON
			JSONArray resRow = new JSONArray();
			JSONObject resCol = null;
			List<String> title = result.get(0);
			
			for (int i=1; i<result.size(); i++) {
				resCol = new JSONObject();
				
				for (int j=0; j<title.size(); j++) {
					resCol.put(title.get(j), result.get(i).get(j));
				}
				
				resRow.add(resCol);
			}
			
			resMsg.addContent("Data", resRow.toString());
		}
		
		return resMsg;
	}

}
