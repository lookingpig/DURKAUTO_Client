package org.lookingpig.DurkAuto.PC;

import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

public class OtherTest {

	@Test
	public void testMD5() {
		System.out.println("----------testMD5----------");
		
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			assertNotNull(md5);

			String source = "哈哈哈哈！";
			System.out.println("source: " + source);
			md5.update(source.getBytes());			
			String md5Text = Base64.encodeBase64String(md5.digest());
			System.out.println("md5Text: " + md5Text);

			source = "啦啦啦啦！";
			md5.update(source.getBytes());			
			md5Text = Base64.encodeBase64String(md5.digest());
			System.out.println("md5Text: " + md5Text);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDateTime() {
		System.out.println("----------testDateTime----------");
		Clock c = Clock.systemDefaultZone();
		System.out.println(c);
		
		Instant i = Instant.now();
		System.out.println(i);
		
		LocalDateTime ldt = LocalDateTime.now();
		System.out.println(ldt);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
		System.out.println(formatter.format(ldt));
	}
	
	@Test
	public void testPath() {
		System.out.println("----------testPath----------");
		String path = Class.class.getResource("/").getPath();
		System.out.println("path: " + path);
		InputStream in = Class.class.getResourceAsStream("/client_config.properties");
		assertNotNull(in);
	}
}
