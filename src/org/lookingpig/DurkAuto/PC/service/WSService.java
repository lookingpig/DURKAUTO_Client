package org.lookingpig.DurkAuto.PC.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import net.sf.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lookingpig.DurkAuto.PC.conf.ClientConfig;
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
	@SuppressWarnings("unchecked")
	private void messageComplete(String message) {
		try {
			// 转换数据格式
			JSONObject msgJson = JSONObject.fromObject(message);
			Iterator<String> i = msgJson.keys();
			String key;
			
			Message msg = new Message();
			msg.setCaller(this);
			msg.setSendNumber(ClientConfig.getConfig("durkauto.pc.sendnumber"));

			while (i.hasNext()) {
				key = i.next();

				if ("username".equals(key)) {
					msg.setSender(msgJson.getString(key));
				} else if ("sendtime".equals(key)) {
					msg.setSendTime(msgJson.getString(key));
				} else {
					msg.addContent(key, msgJson.getString(key));
				}
			}

			// 交由消息服务处理
			Message response = MessageServiceFactory.getFactory().getService(msg.getContent(ClientConfig.MESSAGESERVICE_KEY_NAME))
					.service(msg);

			//有响应消息时回馈客户端
			if (null != response) {
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
		JSONObject msgJson = new JSONObject();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ClientConfig.DATETIME_FORMAT);
		LocalDateTime now = LocalDateTime.now();

		// 加入回应内容
		msgJson.put("sendtime", formatter.format(now));
		Map<String, String> contents = msg.getContents();
		Set<String> keys = contents.keySet();

		for (String key : keys) {
			msgJson.put(key, contents.get(key));
		}

		try {
			session.getBasicRemote().sendText(msgJson.toString());
		} catch (IOException e) {
			logger.error("向客户端发送消息失败！name: " + name, e);
			throw e;
		}
	}
	
	/**
	 * 设置服务名称
	 * @param name 名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 获得服务名称
	 * @return 名称
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 添加一个客户端
	 * @param name 名称
	 * @param service 服务对象
	 */
	public static void addClient(String name, WSService service) {
		clients.put(name, service);
	}
	
	/**
	 * 获得指定客户端
	 * @param name 名称
	 */
	public static WSService getClient(String name) {
		return clients.get(name);
	}
}
