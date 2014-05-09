package org.lookingpig.DurkAuto.PC.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lookingpig.DurkAuto.PC.client.WSClient;
import org.lookingpig.DurkAuto.PC.conf.ClientConfig;
import org.lookingpig.Tools.Service.MessageService.Model.Message;

/**
 * 登陆服务
 * 
 * @author Pig
 * 
 */
@WebServlet("/LoginService")
public class LoginService extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger logger;

	static {
		logger = LogManager.getLogger(LoginService.class);
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginService() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			DateTimeFormatter formatter = DateTimeFormatter
					.ofPattern(ClientConfig.DATETIME_FORMAT);
			LocalDateTime now = LocalDateTime.now();

			Message loginMsg = new Message();
			loginMsg.setSender(username);
			loginMsg.setSendNumber(ClientConfig
					.getConfig("durkauto.pc.sendnumber"));
			loginMsg.setSendTime(formatter.format(now));
			loginMsg.addContent(ClientConfig.MESSAGESERVICE_KEY_NAME, "LoginService");
			loginMsg.addContent("UserName", username);
			loginMsg.addContent("Password", password);

			WSClient.getClient().sendMessage(loginMsg);
		} catch (Exception e) {
			logger.error("登陆服务发生异常！", e);
		}
	}

}
