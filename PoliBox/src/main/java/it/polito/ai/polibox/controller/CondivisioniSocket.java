package it.polito.ai.polibox.controller;

import it.polito.ai.polibox.entity.Condivisione;
import it.polito.ai.polibox.entity.Utente;
import it.polito.ai.polibox.dao.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.Session;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
@ServerEndpoint("/serverWebSocket")
public class CondivisioniSocket {
	private UtenteDAO utenteDAO = (UtenteDAO) AppCtxProv.getApplicationContext().getBean("utenteDAO");
	private CondivisioneDAO condivisioneDAO = (CondivisioneDAO) AppCtxProv.getApplicationContext().getBean("condivisioneDAO");
	@OnMessage
	public void onMessage(Session s, String msg, Model model, EndpointConfig conf) {
		String[] form = msg.split(";");
		
//		boolean allowInvitations;
		boolean allowChanges;
		List<String> usersList = new ArrayList<String>();
		String message;
		String path;
		String filename;
		String email;
		Utente ownerUser; 
		
		email = s.getRequestParameterMap().get("email").get(0);
		//tipologia di operazione
		String type = form[0];
		String[] vetTmp;
		String tmp;
		if (type.equals("NEW"))
		{
	//		vetTmp = tmp.split(": ");
	//		if(vetTmp[1].equals("true"))
	//			allowInvitations = true;
	//		else
	//			allowInvitations = false;
	//		
			tmp = form[1];
			vetTmp = tmp.split(": ");
			if(vetTmp[1].equals("true"))
				allowChanges = true;
			else
				allowChanges = false;
			
			tmp = form[2];
			vetTmp = tmp.split(": ");
			String[] vetTmp2 = vetTmp[1].split(",");
			for(String str: vetTmp2)
				usersList.add(str.trim());
			
			tmp = form[3];
			vetTmp = tmp.split(": ");
			message = vetTmp[1].trim();
			
			tmp = form[4];
			vetTmp = tmp.split(": ");
			path = vetTmp[1];
			
			tmp = form[5];
			vetTmp = tmp.split(": ");
			filename = vetTmp[1];
			
			System.out.println(usersList.toString());
			List<Utente> users = new ArrayList<Utente>();
			for(String str: usersList){
				if(utenteDAO == null)
					System.out.println("utenteDAO è null!!");
				Utente u = utenteDAO.getUtente(str);
				System.out.println(u.toString());
	//			org.hibernate.Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	//			Query query = session.createQuery("from Utente where id = :id");
	//			System.out.println("PIPO");
	//			query.setLong("id", 1);
	//			List<Utente> utenti = new ArrayList<Utente>();
	//			utenti = query.list();
	//			Utente u = null;
	//			if (utenti.size() > 0) {
	//				u = utenti.get(0);
	//			}
	//			System.out.println(u.toString());
				if(u != null)
					users.add(u);
			}
			
			//Aggiungo una riga nella tabella Condivisioni per ogni utente contenuto in users
			Utente owner = utenteDAO.getUtente(email);
			String[] pathElements = path.split("/");
			String pathDir = owner.getHome_dir();
			Log owner_log = new Log(owner.getHome_dir());
			String pathUrl = new String();
			for (int i=5; i<pathElements.length; i++) {
					pathUrl += "\\" + pathElements[i];
			}
			pathDir += "\\Polibox"+ pathUrl;
			System.out.println(pathDir+"-----"+pathUrl+"-------"+filename);
			/*Costruisco una mappa con tutti gli utenti connessi attualmente tramite WebSocket*/
			Map<String, Session> usersConn = new HashMap<String, Session>();
			for(Session s1: s.getOpenSessions()) {
				if(!s1.equals(s) && s1.isOpen())
					usersConn.put(s1.getRequestParameterMap().get("email").get(0), s1);
			}
			for(Utente u: users){
				Condivisione cond = new Condivisione();
				cond.setOwnerId(owner.getId());
				cond.setUserId(u.getId());
				cond.setDirPath(pathDir + "\\" + filename);
				cond.setReadOnly(!allowChanges);
				cond.setState(0);  //stato = 0 => condivisione non ancora accettata dall'utente
				condivisioneDAO.addCondivisione(cond);
				if (usersConn.get(u.getEmail())!=null) {
					usersConn.get(u.getEmail()).getAsyncRemote().sendText("<div>"
							+ "<p><i>" + s.getRequestParameterMap().get("nome").get(0) + "</i> ti ha invitato a condividere la cartella <b>" + filename + "</b></p>"
							+ "<div style='display:none;' id='c_id' name='c_id'>" + cond.getId() + "</div>"
							+ "<button class='btn btn-success' onclick='accept_cond()'>Accetta</button> "
							+ "<button class='btn btn-danger' onclick='refuse_cond()'>Rifiuta</button></div>");
				} else {
					System.out.println(u.getEmail() + " non connesso in questo momento");
				}
				Log log = new Log(u.getHome_dir());
				log.addLine(u.getId(), "RCR","http://localhost:8080/ai/home/"+filename , 0, owner.getId());
				owner_log.addLine(owner.getId(), "RCI",path+"/"+filename , 0, u.getId());
			}
			
		
			/*Inviare a tutti gli utenti di users una mail*/
			//TODO
			
			
//			/*Invio la richiesta di condivisione in tempo reale a tutti gli utenti che sono connessi tramite interfaccia Web*/
//			for(String str: usersList){
//				for(String str1: usersConn.keySet())
//					if(str.equals(str1)){
//						usersConn.get(str1).getAsyncRemote().sendText("<div><p><i>" 
//																		+ s.getRequestParameterMap().get("nome").get(0) 
//																		+ "</i> ti ha invitato a condividere la cartella <b>" 
//																		+ filename 
//																		+ "</b></p> "
//																		+ "<div style='display:none;'  id='c_id' name='c_id' >"
//																		+ utenteDAO.getUtente(s.getRequestParameterMap().get("email").get(0)).getId() 
//																		+ "</div>"
//																		+ " <div style='display:none;'  id='dirPath' name='dirPath' >"+ pathDir +"\\"+ filename+"</div>"
//																		+ " <button class='btn btn-primary' onclick='accept_cond()'>Accetta</button> "
//																		+ "<button class='btn btn-primary' onclick='refuse_cond()'>Rifiuta</button></div>");                
//					}
//			}
			
			/*Notifico all'utente che ha fatto richiesta di condividere la cartella che le richieste sono state inoltrate*/
			s.getAsyncRemote().sendText("<p>La cartella <b>" + filename + "</b> è stata condivisa.<p>");
			
		} else if (type.equals("ACC")) {
			System.out.println("sto accettando una condivisione");
			tmp = form[1];
			vetTmp = tmp.split(": ");
			Long id = Long.parseLong(vetTmp[1]);
			Condivisione cond = condivisioneDAO.getCondivisione(id);
			cond.setState(1);
			condivisioneDAO.updateCondivisione(cond);
			
			Utente owner = utenteDAO.getUtente(cond.getOwnerId());
			Utente u = utenteDAO.getUtente(cond.getUserId());
			Log log = new Log(u.getHome_dir());
			Log owner_log = new Log(owner.getHome_dir());
			String[] p = cond.getDirPath().split("\\\\");
			int flag=0;
			String pp = new String ("http://localhost:8080/ai/home");;
			for (int i=0;i<p.length;i++) {
				if (flag==1) pp+= "/"+p[i];
				else if (p[i].equals("Polibox")) 
					flag=1;
			}
			log.addLine(u.getId(), "RCA","http://localhost:8080/ai/home/"+p[p.length-1] , 0, owner.getId());
			System.out.println("------pp:"+pp);
			owner_log.addLine(owner.getId(), "RCA",pp , 0, u.getId());
		} else if (type.equals("REF")) {
			System.out.println("sto rifiutando una condivisione");
			tmp = form[1];
			vetTmp = tmp.split(": ");
			Long id = Long.parseLong(vetTmp[1]);
			Condivisione cond = condivisioneDAO.getCondivisione(id);
			Utente owner = utenteDAO.getUtente(cond.getOwnerId());
			Utente u = utenteDAO.getUtente(cond.getUserId());
			Log log = new Log(u.getHome_dir());
			Log owner_log = new Log(owner.getHome_dir());
			String[] p = cond.getDirPath().split("\\\\");
			int flag=0;
			String pp = new String("http://localhost:8080/ai/home");
			for (int i=0;i<p.length;i++) {
				if (flag==1) pp += "/"+p[i];
				if (p[i].equals("Polibox")) flag=1;
			}
			condivisioneDAO.deleteCondivisione(cond);
			log.addLine(u.getId(), "RCREF","http://localhost:8080/ai/home/"+p[p.length-1] , 0, owner.getId());
			owner_log.addLine(owner.getId(), "RCREF",pp , 0, u.getId());
		}
	}
	
	@OnOpen
	public void onOpen(Session s, EndpointConfig cfg) {
		System.out.println("Connessione WebSocket aperta con utente: " + s.getRequestParameterMap().get("email").get(0) + ", " + s.getRequestParameterMap().get("nome").get(0));
	}
	
	@OnClose
	public void onClose(Session s, CloseReason reason) {
		System.out.println("Connessione WebSocket chiusa con utente: " + s.getRequestParameterMap().get("email").get(0) + " " + s.getRequestParameterMap().get("nome").get(0));
	}
	
	/*
	@OnError
	public void error(Session s, Throwable t) {
		System.out.println("An ERROR occured!");
		System.out.println(t);
	}*/
	

}
