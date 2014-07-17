package org.lookingpig.DurkAuto.PC.conf;

import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 客户端配置信息
 * @author Pig
 *
 */
public class ClientConfig {
	/**
	 * 日期时间格式化格式
	 */
	public static final String DATETIME_FORMAT = "yyyy-MM-dd hh:mm:ss";
	
	/**
	 * 时间格式化时间
	 */
	public static final String TIME_FORMAT = "hh:mm";
	
	/**
	 * 消息服务
	 */
	public static final String MESSAGESERVICE_KEY = "Message";
	
	/**
	 * 消息服务索引名称
	 */
	public static final String MESSAGESERVICE_KEY_NAME = "ServiceName";
	
	public static final String MESSAGESERVICE_SUB_KEY_NAME = "SubServiceName";
	
	/**
	 * 数据服务索引名称
	 */
	public static final String DATASERVICE_KEY_NAME = "DataServiceName";
	
	/**
	 * 消息服务消息类型
	 */
	public static final String MESSAGESERVICE_TYPE = "MessageType";
	
	/**
	 * 消息服务消息类型-请求
	 */
	public static final String MESSAGESERVICE_TYPE_REQUEST = "Request";
	
	/**
	 * 消息服务消息类型-响应
	 */
	public static final String MESSAGESERVICE_TYPE_RESPONSE = "Response";
	
	/**
	 * 密文与摘要分割符
	 */
	public static final String ENCRYPT_CHECK_SPLIT_MARK = "&";
	
	/**
	 * 摘要算法模式
	 */
	public static final String CHECK_SERVICE_ENCODE_MODE = "MD5";
	
	/**
	 * AES密匙关键字
	 */
	public static final String AES_KEY_KEYWORD = "aeskey";
	
	private static final String CLIENT_CONFIG_PATH = "/client_config.properties";
	
	private static final Logger logger;
	private static final Properties conf;
	
	static {
		logger = LogManager.getLogger(ClientConfig.class);
		conf = new Properties();
		
		try {
			conf.load(ClientConfig.class.getClassLoader().getResourceAsStream(CLIENT_CONFIG_PATH));
		} catch (IOException e) {
			logger.error("加载客户端配置文件失败！path: " + Class.class.getResource("/").getPath(), e);
		}
	}
	
	/**
	 * 获得指定配置信息
	 * @param key 索引
	 * @return 配置信息
	 */
	public static String getConfig(String key) {
		return conf.getProperty(key);
	}
}
