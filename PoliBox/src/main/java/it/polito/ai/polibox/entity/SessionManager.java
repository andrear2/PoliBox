package it.polito.ai.polibox.entity;

import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

public class SessionManager {

	private ConcurrentHashMap<Long, ConcurrentHashMap<Long, Session>> openedSessions;
	
	
	private static class SessionManagerHolder{
		private final static SessionManager INSTANCE = new SessionManager(); 
	}
	
	public static SessionManager getInstance(){
		return SessionManagerHolder.INSTANCE;
	}
	
	private SessionManager(){
		openedSessions = new ConcurrentHashMap<Long, ConcurrentHashMap<Long,Session>>();
	}
	
	public void addUserSession(Long id_user,Long id_disp,Session s){

		if(openedSessions.containsKey(id_user)){
			openedSessions.get(id_user).put(id_disp, s);
		}else{
			ConcurrentHashMap<Long, Session> tmp = new ConcurrentHashMap<Long, Session>();
			tmp.put(id_disp, s);
			openedSessions.put(id_user, tmp);
		}
	}
	public ConcurrentHashMap<Long, Session> getSessionMap(Long id){
		if(openedSessions.containsKey(id))
			return openedSessions.get(id);
		else
			return null;
	}

	public void removeUserSession(Long id, Long disp) {
		System.out.println("Rimuovo da utente:"+id+"disp:"+disp);
		if (openedSessions.get(id).size() == 1) {
			openedSessions.remove(id);
			System.out.println("un dispostivo solo, elimino utente da mappa");
		} else {
			openedSessions.get(id).remove(disp);
			System.out.println("più disp elimino solo quello");
		}
		
	}
}
