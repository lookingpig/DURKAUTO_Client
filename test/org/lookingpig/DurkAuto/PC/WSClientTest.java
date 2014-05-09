package org.lookingpig.DurkAuto.PC;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import javax.websocket.DeploymentException;
import javax.websocket.Session;

import org.junit.Test;
import org.lookingpig.DurkAuto.PC.client.WSClient;

public class WSClientTest {

	@Test
	public void testConnect() {
		String uri = "ws://localhost:8080/CarWithService/service/wsService";
		
		try {
			Session session = WSClient.connect(uri);
			assertNotNull(session);
			session.getBasicRemote().sendText("Hello world!");
		} catch (DeploymentException e) {
			e.printStackTrace();
			fail("�����쳣��");
		} catch (IOException e) {
			e.printStackTrace();
			fail("��/д�쳣��");
		}
	}

}
