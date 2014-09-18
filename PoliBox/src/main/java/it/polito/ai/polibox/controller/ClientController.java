package it.polito.ai.polibox.controller;

import it.polito.ai.polibox.dao.UtenteDAO;
import it.polito.ai.polibox.entity.SessionManager;
import it.polito.ai.polibox.entity.Utente;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Controller;

@Controller
@ServerEndpoint(value="/client")
public class ClientController {
	private String fileName;
	private String pathUtente;
	private boolean firstRequest;
	private UtenteDAO utenteDAO = (UtenteDAO) AppCtxProv.getApplicationContext().getBean("utenteDAO");
	static boolean connected=false;
	private Utente u;
	private Long id;
	private Long disp;
	
	@OnOpen
	public void onOpen(Session s, EndpointConfig cfg) {
		System.out.println("Aperta!");
		id = utenteDAO.getUtente(s.getRequestParameterMap().get("user").get(0)).getId(); 
		disp = Long.parseLong(s.getRequestParameterMap().get("disp").get(0));
		SessionManager.getInstance().addUserSession(id,disp, s);
	}
	@OnClose
	public void onClose(Session s){
		
		System.out.println("Sessione chiusaaaa!!!!!");
	}
	@OnMessage
	public void onMessage(Session session, String msg) {
		System.out.println("RICEVUTO! " + msg);
		String[] s = msg.split(":");
		pathUtente = new String();
		if (s[0].equals("DIR")) {
			u = utenteDAO.getUtente(s[1]);
			fileName = "C:\\Polibox uploaded files\\" + u.getId() + "_" + u.getCognome() + "_" + u.getNome() + "\\Polibox\\" + s[2];
			File f = new File(fileName);
			f.mkdir();
		} else if (s[0].equals("DELETE_FILE")) {
			u = utenteDAO.getUtente(s[1]);
			fileName = "C:\\Polibox uploaded files\\" + u.getId() + "_" + u.getCognome() + "_" + u.getNome() + "\\Polibox\\" + s[2];
			File f = new File(fileName);
			f.delete();
			if (f.isDirectory()) {
				try {
					FileUtils.deleteDirectory(f);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else if (s[0].equals("DELETE_DIR")) {
			u = utenteDAO.getUtente(s[1]);
			fileName = "C:\\Polibox uploaded files\\" + u.getId() + "_" + u.getCognome() + "_" + u.getNome() + "\\Polibox\\" + s[2];
			File f = new File(fileName);
			try {
				FileUtils.deleteDirectory(f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (s[0].equals("EMPTY_FILE")) {
			File f = new File(pathUtente + s[1]);
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			u = utenteDAO.getUtente(s[0]);
			fileName = "C:\\Polibox uploaded files\\" + u.getId() + "_" + u.getCognome() + "_" + u.getNome() + "\\Polibox\\" + s[1];
			firstRequest = true;
		}
	}

	@OnMessage
	public void onBinaryMessage(Session session, ByteBuffer buf) {
		try {
			File f = new File(fileName);
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
