package org.lookingpig.DurkAuto.PC.service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lookingpig.DurkAuto.PC.conf.ClientConfig;
import org.lookingpig.DurkAuto.PC.conf.StateCode;
import org.lookingpig.Tools.Service.MessageService.MessageService;
import org.lookingpig.Tools.Service.MessageService.MessageServiceFactory;
import org.lookingpig.Tools.Service.MessageService.Model.Message;

/**
 * WebSocket服务端
 * 
 * @author Pig
 * 
 */
@ServerEndpoint("/service/wsService")
public class WSService {
	private static final Logger logger;
	private static final AtomicInteger connectionTotal;
	private static final Map<String, WSService> clients;

	private Session session;
	private String name;
	private StringBuffer largeMessage;

	static {
		logger = LogManager.getLogger(WSService.class);
		connectionTotal = new AtomicInteger(0);
		clients = new ConcurrentHashMap<>();
	}

	public WSService() {
		largeMessage = new StringBuffer();
	}

	@OnOpen
	public void onOpen(Session session) {
		// 累计每日连接量
		logger.info("一个连接已建立，当前连接数：" + connectionTotal.getAndIncrement());
		this.session = session;
	}

	@OnClose
	public void onClose(CloseReason cr) {
		logger.info("一个连接已关闭，原因：" + cr.getReasonPhrase());
		clients.remove(name);
	}

	@OnError
	public void onError(Throwable t) {
		logger.error("连接发生异常！", t);
	}

	@OnMessage
	public void onMessage(Session session, String message, boolean last) {
		largeMessage.append(message);

		if (last) {
			System.out.println("接收到大文本：" + largeMessage.toString());
			messageComplete(largeMessage.toString());
			largeMessage.delete(0, largeMessage.length());
		}
	}

	/**
	 * 接收到一条完整的消息
	 * 
	 * @param message
	 *            消息
	 */
	private void messageComplete(String message) {
		try {
			Message msg = Service.jsonToMessage(message);
			msg.setCaller(this);

			// 交由消息服务处理
			String serviceName = msg.getContent(ClientConfig.MESSAGESERVICE_KEY_NAME);
			MessageService service = MessageServiceFactory.getFactory().getService(serviceName);
			Message response = null;
				
			if (null == service) {
				logger.warn("未找到所需的服务，服务名称：" + serviceName);
				// 未找到指定服务
				response = new Message();
				response.addContent(StateCode.FLAG, StateCode.FALL_MESSAGESERVICE_NOTFOUND);
			} else {
				response = service.service(msg);
			}
			
			// 有响应消息时回馈客户端
			if (null != response) {
				response.addContent(ClientConfig.MESSAGESERVICE_KEY_NAME, msg.getContent(ClientConfig.MESSAGESERVICE_KEY_NAME));
				response.addContent(ClientConfig.MESSAGESERVICE_TYPE, ClientConfig.MESSAGESERVICE_TYPE_RESPONSE);
				sendMessage(response);
			}
		} catch (Exception e) {
			logger.error("接收到一条异常的消息！message: " + message, e);
		}
	}

	/**
	 * 向客户端发送消息
	 * 
	 * @param msg
	 *            消息
	 * @throws IOException
	 *             发送消息失败
	 */
	public void sendMessage(Message msg) throws IOException {
		try {
			String jsonStr = Service.messageToJson(msg);

			session.getBasicRemote().sendText(jsonStr);
		} catch (IOException e) {
			logger.error("向客户端发送消息失败！name: " + name, e);
			throw e;
		}
	}

	/**
	 * 设置服务名称
	 * 
	 * @param name
	 *            名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获得服务名称
	 * 
	 * @return 名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 添加一个客户端
	 * 
	 * @param name
	 *            名称
	 * @param service
	 *            服务对象
	 */
	public static void addClient(String name, WSService service) {
		clients.put(name, service);
	}

	/**
	 * 获得指定客户端
	 * 
	 * @param name
	 *            名称
	 */
	public static WSService getClient(String name) {
		return clients.get(name);
	}
}
