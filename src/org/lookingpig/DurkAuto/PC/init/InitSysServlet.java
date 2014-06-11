package org.lookingpig.DurkAuto.PC.init;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.websocket.DeploymentException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lookingpig.DurkAuto.PC.client.WSClient;
import org.lookingpig.DurkAuto.PC.conf.ClientConfig;
import org.lookingpig.Tools.Service.MessageService.MessageServiceFactory;

/**
 * 用于初始化
 * @author Pig
 *
 */
public class InitSysServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(InitSysServlet.class);
	  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InitSysServlet() {
        super();
    }
    
    @Override
    public void init(ServletConfig config) throws ServletException {
    	super.init(config);
    	logger.info("----------开始初始化DurkAuto客户端服务----------");
    	super.init(config);
    	
    	logger.info("1，初始化消息服务");
    	File msgSerCof = new File(Class.class.getResource("/").getPath() + ClientConfig.getConfig("durkauto.pc.messageservice.config.path"));
    	MessageServiceFactory.getFactory().loadServices(msgSerCof);
    	
    	logger.info("2，与远程服务器建立连接");
    	try {
    		//与远程服务器建立连接
    		WSClient.connect(ClientConfig.getConfig("durkauto.server.host"));
    	} catch (DeploymentException e) {
    		logger.error("WebSocket部署异常！", e);
    	} catch (IOException e) {
    		logger.error("WebSocket读/写异常！", e);
    	}
    	
    	logger.info("----------成功初始化DurkAuto客户端服务----------");
    }
}
