package org.lookingpig.DurkAuto.PC.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lookingpig.DurkAuto.PC.conf.ClientConfig;
import org.lookingpig.DurkAuto.PC.conf.StateCode;
import org.lookingpig.Tools.Service.MessageService.MessageService;
import org.lookingpig.Tools.Service.MessageService.MessageServiceFactory;
import org.lookingpig.Tools.Service.MessageService.Model.Message;

/**
 * 提供消息服务
 */
@WebServlet("/Service")
public class Service extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(Service.class);

	public Service() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		pw.write("错误的消息格式！");
		pw.flush();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		logger.info("接收到一个服务请求");
		
		try {
			String message = request.getParameter(ClientConfig.MESSAGESERVICE_KEY);
			logger.info("JSON消息：" + message);
			Message msg = jsonToMessage(message);
			logger.info("Message消息：" + msg);
			msg.setCaller(this);
			String serviceName = msg.getContent(ClientConfig.MESSAGESERVICE_KEY_NAME);

			//获得消息服务
			MessageService service = MessageServiceFactory.getFactory().getService(serviceName);

			if (null == service) {
				logger.warn("未找到所需的服务，服务名称：" + serviceName);
				//未找到指定服务
				response.sendRedirect("/404.html");
			} else {
				String respStr = "";
				Message respMsg = service.service(msg);

				if (null != respMsg) {
					respStr = messageToJson(respMsg);
				} else {
					logger.warn("委托服务未返回任何数据");
					//未返回信息属未知错误
					respStr = getErrorMessage();
				}

				PrintWriter pw = response.getWriter();
				pw.write(respStr);
				pw.flush();
			}
		} catch (Exception e) {
			logger.error("消息服务处理失败！原因：", e);
			
			//返回错误信息
			PrintWriter pw = response.getWriter();
			pw.write(getErrorMessage());
			pw.flush();
		}
	}

	private String getErrorMessage() {
		JSONObject json = new JSONObject();
		json.put(StateCode.FLAG, StateCode.FALL_UNKNOWN);
		json.put(StateCode.DESCRIBE_FLAG, "未知的错误！");
		return json.toString();
	}
	
	/**
	 * 将JSON格式转换为Message
	 * 
	 * @param json
	 *            json文本
	 * @return Message对象
	 */
	@SuppressWarnings("unchecked")
	public static Message jsonToMessage(String json) {
		logger.info("开始转换JSON");
		Message msg = new Message();

		try {
			// 转换数据格式
			JSONObject msgJson = JSONObject.fromObject(json);
			Iterator<String> i = msgJson.keys();
			String key;

			msg.setSendNumber(ClientConfig.getConfig("durkauto.pc.sendnumber"));
			
			while (i.hasNext()) {
				key = i.next();

				if ("username".equals(key)) {
					msg.setSender(msgJson.getString(key));
				} else if ("sendtime".equals(key)) {
					msg.setSendTime(msgJson.getString(key));
				}
				
				msg.addContent(key, msgJson.getString(key));
			}
		} catch (Exception e) {
			logger.error("将JSON格式转换为Message时出错，原因：", e);
		}

		return msg;
	}

	/**
	 * 将Message转换成Json格式
	 * 
	 * @param message
	 *            消息对象
	 * @return json文本
	 */
	public static String messageToJson(Message message) {
		JSONObject msgJson = new JSONObject();
		
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ClientConfig.DATETIME_FORMAT);
			LocalDateTime now = LocalDateTime.now();

			// 加入回应内容
			msgJson.put("sendtime", formatter.format(now));
			Map<String, String> contents = message.getContents();
			Set<String> keys = contents.keySet();

			for (String key : keys) {
				msgJson.put(key, contents.get(key));
			}
		} catch (Exception e) {
			logger.error("将Message转换成Json格式时出错，原因：", e);
		}
		
		return msgJson.toString();
	}
}
