package org.lookingpig.DurkAuto.PC.service;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lookingpig.DurkAuto.PC.conf.ClientConfig;
import org.lookingpig.DurkAuto.PC.conf.StateCode;
import org.lookingpig.Tools.Ciphertext.AESCryptographService;
import org.lookingpig.Tools.Ciphertext.CryptographService;
import org.lookingpig.Tools.Service.MessageService.MessageFactory;
import org.lookingpig.Tools.Service.MessageService.MessageService;
import org.lookingpig.Tools.Service.MessageService.MessageServiceFactory;
import org.lookingpig.Tools.Service.MessageService.Model.Message;
import org.lookingpig.Tools.Service.Socket.ClientEvent;
import org.lookingpig.Tools.Service.Socket.EventListener;
import org.lookingpig.Tools.Service.Socket.NIOSocketServer;

/**
 * Socket服务
 * @author Pig
 *
 */
public class SocketService {
	private static final Logger logger;
	private static final CryptographService cryptograph;
	private static SocketService service;
	
	private NIOSocketServer server;
	private ClientEventListener listener;
	private int serverPort;
	private String serverHost;
	private String aesKey;
	
	static {
		logger = LogManager.getLogger(SocketService.class);
		cryptograph = new AESCryptographService();
	}
	
	private SocketService() {
		aesKey = ClientConfig.getConfig("durkauto.pc.messageservice.initial.key");
		String messageCharset = ClientConfig.getConfig("durkauto.server.message.charset");
		int messageBufSize = Integer.parseInt(ClientConfig.getConfig("durkauto.server.message.buf.size"));
		serverPort = Integer.parseInt(ClientConfig.getConfig("durkauto.server.port"));
		serverHost = ClientConfig.getConfig("durkauto.server.host");
		
		listener = new ClientEventListener();
		server = new NIOSocketServer(messageCharset, messageBufSize);
		server.register(NIOSocketServer.CLIENTEVENT_CONNECT, listener);
		server.register(NIOSocketServer.CLIENTEVENT_RECEIVE, listener);
	}
	
	/**
	 * 获得服务
	 * @return 服务
	 */
	public static SocketService getService() {
		if (null == service) {
			service = new SocketService();
		}
		
		return service;
	}
	
	/**
	 * 启动服务
	 */
	public void start() {
		server.start(serverHost, serverPort);
	}
	
	/**
	 * 关闭服务
	 */
	public void shutdown() {
		server.shutdown();
	}
	
	/**
	 * 向客户端发送消息
	 * @param message 消息
	 */
	public void sendMessage(NIOSocketServer.Client client, Message message) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ClientConfig.DATETIME_FORMAT);
		LocalDateTime now = LocalDateTime.now();
		message.setSendTime(formatter.format(now));
		String ciphertext = encrypt(message, aesKey);
		
		if (null != ciphertext) {
			client.sendMessage(ciphertext);
		}
	}
	
	/**
	 * 向客户端发送本地消息
	 * @param message
	 */
	public void sendLocalMessage(NIOSocketServer.Client client, Message message) {
		if (null != message) {
			message.setSender(ClientConfig.getConfig("durkauto.pc.sender"));
			message.setSendNumber(ClientConfig.getConfig("durkauto.pc.sendnumber"));
			sendMessage(client, message);
		}
	}
	
	/**
	 * 向所有客户端发送消息
	 * @param message 消息
	 */
	public void sendMessageToAll(Message message) {
		for (NIOSocketServer.Client client : server.getClients()) {
			sendLocalMessage(client, message);
		}
	}
	
	/**
	 * 设置所有客户端的属性
	 * @param key 键
	 * @param value 值
	 */
	public void setAttrbuteToAll(String key, String value) {
		for (NIOSocketServer.Client client : server.getClients()) {
			client.setAttribute(key, value);
		}
	}
	
	/**
	 * 加密消息
	 * @param message 消息
	 * @return 加密后的消息
	 */
	private String encrypt(Message message, String aesKey) {
		String ciphertext = null;
		
		try {
			String strMsg = MessageFactory.getFactory().blend(message);
			// 加密
			strMsg = cryptograph.encrypt(strMsg, aesKey);
			// 加入校验信息
			MessageDigest mdInst = MessageDigest.getInstance(ClientConfig.CHECK_SERVICE_ENCODE_MODE);
			mdInst.update((strMsg + message.getSendTime()).getBytes());
			ciphertext = strMsg +ClientConfig.ENCRYPT_CHECK_SPLIT_MARK + Base64.encodeBase64String(mdInst.digest());
		} catch (InvalidKeyException e) {
			logger.error("加密密匙格式不对！key: " + aesKey, e);
		} catch (NoSuchAlgorithmException e) {
			logger.error("MD5摘要服务实例化失败！", e);
		} catch (Exception e) {
			logger.error("消息加密失败！原因：", e);
		}
		
		return ciphertext;
	}
	
	/**
	 * 解密消息
	 * @param ciphertext 加密的消息
	 * @return 消息
	 */
	private Message decrypt(String ciphertext, String aesKey) {
		Message message = null;

		try {
			// 校验格式
			if (ciphertext.contains(ClientConfig.ENCRYPT_CHECK_SPLIT_MARK)) {
				String[] content = ciphertext.split(ClientConfig.ENCRYPT_CHECK_SPLIT_MARK);

				if (2 == content.length) {

					String strMsg = cryptograph.decrypt(content[0], aesKey);
					message = MessageFactory.getFactory().resolve(strMsg);

					// 校验是否被修改
					MessageDigest mdInst = MessageDigest.getInstance(ClientConfig.CHECK_SERVICE_ENCODE_MODE);
					mdInst.update((content[0] + message.getSendTime()).getBytes());
					String checkStr = Base64.encodeBase64String(mdInst.digest());

					if (!content[1].equals(checkStr)) {
						logger.warn("消息已被修改！message: " + message);
						message = null;
					}
				} else {
					logger.warn("消息格式错误！message: " + message);
					message = null;
				}
			} else {
				logger.warn("消息格式错误！message: " + ciphertext);
				message = null;
			}
		} catch (InvalidKeyException e) {
			logger.error("解密密匙格式不对！key: " + aesKey, e);
			message = null;
		} catch (NoSuchAlgorithmException e) {
			logger.error("MD5摘要服务实例化失败！", e);
			message = null;
		} catch (Exception e) {
			logger.error("消息解密失败！消息：" + ciphertext + "，原因：", e);
			message = null;
		}

		return message;
	}
	
	/**
	 * 当有客户端连接时调用
	 * @param client 客户端
	 */
	private void onConnect(NIOSocketServer.Client client) {
		logger.info("客户端连接事件发生，地址：" + client.getChannel().socket());
	}
	
	/**
	 * 当有客户端消息时调用
	 * @param client 客户端
	 * @param message 消息
	 */
	private void onMessage(NIOSocketServer.Client client, String message) {
		logger.info("客户端消息事件发生，地址：" + client.getChannel().socket() + "，消息：" + message);
		Message msg = decrypt(message, aesKey);
		
		if (null != msg) {
			msg.setCaller(client);
			
			// 交由消息服务处理
			String serviceName = msg.getContent(ClientConfig.MESSAGESERVICE_KEY_NAME);
			MessageService service = MessageServiceFactory.getFactory().getService(serviceName);
			Message response;
			
			if (null != service) {
				response = service.service(msg);
			} else {
				logger.warn("未找到所需的服务，服务名称：" + serviceName);
				// 未找到指定服务
				response = new Message();
				response.addContent(StateCode.FLAG, StateCode.FALL_MESSAGESERVICE_NOTFOUND);
			}
			
			// 有响应消息时回馈客户端
			if (null != response) {
				response.addContent(ClientConfig.MESSAGESERVICE_KEY_NAME, msg.getContent(ClientConfig.MESSAGESERVICE_KEY_NAME));
				response.addContent(ClientConfig.MESSAGESERVICE_TYPE, ClientConfig.MESSAGESERVICE_TYPE_RESPONSE);
				
				//子服务类型
				if (msg.getContents().containsKey(ClientConfig.MESSAGESERVICE_SUB_KEY_NAME)) {
					response.addContent(ClientConfig.MESSAGESERVICE_SUB_KEY_NAME, msg.getContent(ClientConfig.MESSAGESERVICE_SUB_KEY_NAME));
				}
				
				sendLocalMessage(client, response);
			}
		}
	}
	
	/**
	 * 设置AES密匙
	 * @param aesKey 密匙 
	 */
	public void setAESKey(String aesKey) {
		this.aesKey = aesKey;
	}
	
	/**
	 * 客户端事件监听器
	 * @author Pig
	 *
	 */
	private class ClientEventListener implements EventListener {

		@Override
		public void onEvent(ClientEvent event) {			
			switch (event.getType()) {
			case NIOSocketServer.CLIENTEVENT_CONNECT:
				onConnect(event.getClient());
				break;
			case NIOSocketServer.CLIENTEVENT_RECEIVE:
				onMessage(event.getClient(), event.getMessage());
				break;
			}
		}
	}
}

