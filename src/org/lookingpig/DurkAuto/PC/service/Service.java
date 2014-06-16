package org.lookingpig.DurkAuto.PC.service;

import java.io.IOException;
import java.io.PrintWriter;

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
		try {
			String message = request.getParameter(ClientConfig.MESSAGESERVICE_KEY);
			Message msg = WSService.JsonToMessage(message);
			msg.setCaller(this);
			String serviceName = msg.getContent(ClientConfig.MESSAGESERVICE_KEY_NAME);

			//获得消息服务
			MessageService service = MessageServiceFactory.getFactory().getService(serviceName);

			if (null == service) {
				//未找到指定服务
				response.sendRedirect("/404.html");
			} else {
				String respStr = "";
				Message respMsg = service.service(msg);

				if (null != respMsg) {
					respStr = WSService.MessageToJson(respMsg);
				} else {
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
}
