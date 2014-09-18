package it.polito.ai.polibox.controller;

import it.polito.ai.polibox.dao.DispositivoDAO;
import it.polito.ai.polibox.dao.SincronizzazioniPendentiDAO;
import it.polito.ai.polibox.dao.UtenteDAO;
import it.polito.ai.polibox.entity.Dispositivo;
import it.polito.ai.polibox.entity.SessionManager;
import it.polito.ai.polibox.entity.SincronizzazioniPendenti;
import it.polito.ai.polibox.entity.Utente;

import java.io.File;
import java.io.FileInputStream;
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
	private DispositivoDAO dispositivoDAO = (DispositivoDAO) AppCtxProv.getApplicationContext().getBean("dispositivoDAO");
	private SincronizzazioniPendentiDAO sincDAO = (SincronizzazioniPendentiDAO) AppCtxProv.getApplicationContext().getBean("sincDAO");
	static boolean connected=false;
	private Utente u;
	private Long id;
	private Long disp;
	
	@OnOpen
	public void onOpen(Session s, EndpointConfig cfg) {
		System.out.println("Aperta!");
		Utente u = utenteDAO.getUtente(s.getRequestParameterMap().get("user").get(0)); 
		id = u.getId();
		disp = Long.parseLong(s.getRequestParameterMap().get("disp").get(0));
		SessionManager.getInstance().addUserSession(id,disp, s);
		Dispositivo d = dispositivoDAO.getDispositivo(disp);
		if (d.isNew()) {
			visitDirectory(new File(u.getHome_dir() + "\\Polibox"));
			d.setNew(false);
			dispositivoDAO.updateDispositivo(d);
		}
		for (SincronizzazioniPendenti sp: sincDAO.getSincronizzazioniPendenti(id, disp)) {
			switch(sp.getType()) {
			case 0:
				sendDirectory(new File(sp.getPath()));
				break;
			case 1:
				break;
			case 2:
				break;
			default:
				break;
			}
		}
		
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

	private void visitDirectory(File dir){
		if(dir.isFile())
			sendFile(dir);
		else{
			sendDirectory(dir);
			String[] children = dir.list();
			if (children != null) {
				for(int i = 0; i < children.length; i++) {
					System.out.println("--->" + dir.getAbsolutePath() + "\\" + children[i]);
					visitDirectory(new File(dir.getAbsolutePath() + "\\" + children[i])); 
				}
			}
		}
	}
	private void sendDirectory(File dir) {
		for (Session s : SessionManager.getInstance().getSessionMap(id).values()){
			try {
				System.out.println("pathD " + dir.getAbsolutePath() + " " + dir.getName() + " " + dir.getAbsolutePath().split("Polibox").length);
				if (dir.getAbsolutePath().split("Polibox").length > 2) {
					s.getBasicRemote().sendText("DIR:"+dir.getAbsolutePath().split("Polibox\\\\")[1]);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private void sendFile(File dir) {
		try {
			for (Session s : SessionManager.getInstance().getSessionMap(id).values()){
				System.out.println("pathF " + dir.getAbsolutePath() + " " + dir.getName() + " " + dir.getAbsolutePath().split("Polibox\\\\").length);
				if (dir.getAbsolutePath().split("Polibox\\\\").length > 1) {
					s.getBasicRemote().sendText("FILE:"+dir.getAbsolutePath().split("Polibox\\\\")[1]);
				}
					
			}
			FileInputStream fis=null;
			fis = new FileInputStream(dir);
			System.out.println(fis.available());
			byte[] b = new byte[8192];
			int bRead;
			ByteBuffer bb;
			while ((bRead=fis.read(b))!=-1) {
				System.out.println(bRead);
				bb=ByteBuffer.wrap(b);
					for (Session s : SessionManager.getInstance().getSessionMap(id).values()){
						s.getBasicRemote().sendBinary(bb);
					}
				bb.clear();
				Thread.sleep(25);
			}
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
