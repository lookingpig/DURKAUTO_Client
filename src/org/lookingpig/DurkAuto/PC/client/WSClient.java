package org.lookingpig.DurkAuto.PC.client;

import java.io.IOException;
import java.net.URI;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lookingpig.DurkAuto.PC.conf.ClientConfig;
import org.lookingpig.Tools.Ciphertext.AESCryptographService;
import org.lookingpig.Tools.Ciphertext.CryptographService;
import org.lookingpig.Tools.Service.MessageService.MessageFactory;
import org.lookingpig.Tools.Service.MessageService.MessageServiceFactory;
import org.lookingpig.Tools.Service.MessageService.Model.Message;

/**
 * WebSocket客户端
 * 
 * @author Pig
 * 
 */
@ClientEndpoint
public class WSClient {
	private static final Logger logger;
	private static final List<WSClient> clientPool;
	private static final CryptographService cryptograph;
	private static final AtomicInteger connectionTotal;

	private StringBuffer largeMessage;
	private Session session;
	private String aesKey;
	private boolean idle;
	private int id;

	static {
		logger = LogManager.getLogger(WSClient.class);
		clientPool = new ArrayList<>();
		cryptograph = new AESCryptographService();
		connectionTotal = new AtomicInteger(0);
	}

	public WSClient() {
		idle = false;
		largeMessage = new StringBuffer();
	}

	@OnOpen
	public void onOpen(Session session) {
		id = connectionTotal.incrementAndGet();
		this.session = session;
		aesKey = (String) session.getUserProperties().get(ClientConfig.AES_KEY_KEYWORD);
		clientPool.add(this);
		logger.info("已经与服务器建立连接。id: " + this.session.getId());

		//发送上线消息
		Message msg = new Message();
		msg.setSender(ClientConfig.getConfig("durkauto.pc.sender") + "_" + id);
		msg.setSendNumber(ClientConfig.getConfig("durkauto.pc.sendnumber"));
		msg.addContent(ClientConfig.MESSAGESERVICE_KEY_NAME, "OnLineService");

		try {
			sendMessage(msg);
		} catch (IOException e) {
			logger.error("向服务器发送上线消息失败，原因：", e);
		}
	}

	@OnClose
	public void onClose(CloseReason cr) {
		logger.info("已经与服务器断开连接。原因：" + cr.getReasonPhrase());
	}

	@OnError
	public void OnError(Throwable t) {
		logger.error("与服务器通信发生异常！", t);
	}

	@OnMessage
	public void onMessage(Session session, String message, boolean last) {
		largeMessage.append(message);

		if (last) {
			logger.info("接收到一条来自服务器的消息：" + largeMessage.toString());
			messageComplete(largeMessage.toString());
			largeMessage.delete(0, largeMessage.length());
		}
	}

	/**
	 * 接收到一条完整的消息
	 * 
	 * @param message
	 *            完整的消息
	 */
	private void messageComplete(String message) {
		try {
		// 校验格式
		if (!message.contains(ClientConfig.ENCRYPT_CHECK_SPLIT_MARK)) {
			logger.warn("接收到一条非法消息！message: " + message);
			return;
		}

		String[] content = message.split(ClientConfig.ENCRYPT_CHECK_SPLIT_MARK);

		if (2 != content.length) {
			logger.warn("接收到一条非法消息！message: " + message);
			return;
		}

		// 解密
		message = cryptograph.decrypt(content[0], aesKey);
		Message msg = MessageFactory.getFactory().resolve(message);

		// 校验是否被修改
		MessageDigest mdInst = MessageDigest.getInstance(ClientConfig.CHECK_SERVICE_ENCODE_MODE);
		mdInst.update((content[0] + msg.getSendTime()).getBytes());
		String checkStr = Base64.encodeBase64String(mdInst.digest());

		if (!content[1].equals(checkStr)) {
			logger.warn("接收到一条被修改的消息！message: " + message);
			return;
		}

		// 交由消息服务处理
		MessageServiceFactory.getFactory().getService(msg.getContent(ClientConfig.MESSAGESERVICE_KEY_NAME))
				.service(msg);
		} catch (InvalidKeyException e) {
			logger.error("解密密匙格式不对！key: " + aesKey, e);
			return;
		} catch (NoSuchAlgorithmException e) {
			logger.error("MD5摘要服务实例化失败！", e);
		} catch (Exception e) {
			logger.error("消息处理失败！message: " + message, e);
		}
	}

	/**
	 * 向服务器发送消息
	 * 
	 * @param message
	 *            消息对象
	 * @throws IOException
	 *             消息发送失败
	 */
	public void sendMessage(Message message) throws IOException {
		String strMsg = null;
		
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ClientConfig.DATETIME_FORMAT);
			LocalDateTime now = LocalDateTime.now();
			message.setSendTime(formatter.format(now));
			
			strMsg = MessageFactory.getFactory().blend(message);
			// 加密
			strMsg = cryptograph.encrypt(strMsg, aesKey);

			// 加入校验信息
			MessageDigest mdInst = MessageDigest.getInstance(ClientConfig.CHECK_SERVICE_ENCODE_MODE);
			mdInst.update((strMsg + message.getSendTime()).getBytes());
			strMsg += ClientConfig.ENCRYPT_CHECK_SPLIT_MARK + Base64.encodeBase64String(mdInst.digest());

			session.getBasicRemote().sendText(strMsg);
		} catch (InvalidKeyException e) {
			logger.error("加密密匙格式不对！key: " + aesKey, e);
		} catch (NoSuchAlgorithmException e) {
			logger.error("MD5摘要服务实例化失败！", e);
		} catch (IOException e) {
			logger.error("与服务器通信发生异常！", e);
			throw e;
		} catch (Exception e) {
			logger.error("向服务器发送消息失败！msg: " + strMsg, e);
		}
	}

	/**
	 * 与服务器建立连接
	 * 
	 * @param uri
	 *            服务器路径
	 * @return 会话
	 * @throws DeploymentException
	 *             部署存在问题
	 * @throws IOException
	 *             读/写问题
	 */
	public static Session connect(String uri) throws DeploymentException, IOException {
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		return container.connectToServer(WSClient.class, URI.create(uri));
	}

	/**
	 * 设置空闲状态
	 * @param idle 是否空闲
	 */
	public void setIdle(boolean idle) {
		this.idle = idle;
	}
	
	/**
	 * 获取空闲状态
	 * @return 是否空闲
	 */
	public boolean isIdle() {
		return idle;
	}
	
	/**
	 * 获得指定客户端
	 * 
	 * @param key
	 *            索引
	 * @return 客户端
	 */
	public static WSClient getClient() {
		
		for (WSClient client : clientPool) {
			if (client.isIdle()) {
				return client;
			}
		}
		
		// TODO池功能待实现
		return null;
	}
	
	/**
	 * 添加一个客户端
	 * @param client 客户端
	 */
	public static void addClient(WSClient client) {
		clientPool.add(client);
	}
}
