package it.polito.ai.polibox.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.websocket.EndpointConfig;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Controller;

@Controller
@ServerEndpoint("/client")
public class ClientController {
	private String fileName;
	private boolean firstRequest;
	
	@OnOpen
	public void onOpen(Session s, EndpointConfig cfg) {
		System.out.println("Aperta!");
	}
	
	@OnMessage
	public void onMessage(Session session, String msg) {
		System.out.println("RICEVUTO! " + msg);
		String[] s = msg.split(":");
		if (s[0].equals("DIR")) {
			File f = new File("C:\\Polibox uploaded files" + s[1]);
			f.mkdir();
		} else if (s[0].equals("DELETE_FILE")) {
			File f = new File("C:\\Polibox uploaded files" + s[1]);
			f.delete();
		} else if (s[0].equals("DELETE_DIR")) {
			File f = new File("C:\\Polibox uploaded files" + s[1]);
			try {
				FileUtils.deleteDirectory(f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (s[0].equals("EMPTY_FILE")) {
			File f = new File("C:\\Polibox uploaded files" + s[1]);
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			fileName = msg;
			firstRequest = true;
		}
	}
	
	@OnMessage
	public void onBinaryMessage(Session session, ByteBuffer buf) {
		try {
			File f = new File("C:\\Polibox uploaded files" + fileName);
			FileOutputStream fos;
			if (firstRequest) {
				fos = new FileOutputStream(f);
			} else {
				fos = new FileOutputStream(f, true);
			}
			firstRequest = false;
			while (buf.hasRemaining()) {
				fos.write(buf.get());
			}
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
