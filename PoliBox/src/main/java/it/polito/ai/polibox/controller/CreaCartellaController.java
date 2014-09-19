package it.polito.ai.polibox.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import it.polito.ai.polibox.dao.CondivisioneDAO;
import it.polito.ai.polibox.dao.DispositivoDAO;
import it.polito.ai.polibox.dao.SincronizzazioniPendentiDAO;
import it.polito.ai.polibox.dao.SincronizzazioniPendentiDAOImpl;
import it.polito.ai.polibox.dao.UtenteDAO;
import it.polito.ai.polibox.entity.Condivisione;
import it.polito.ai.polibox.entity.Dispositivo;
import it.polito.ai.polibox.entity.SessionManager;
import it.polito.ai.polibox.entity.SincronizzazioniPendenti;
import it.polito.ai.polibox.entity.Utente;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CreaCartellaController implements CheckConnection{
	@Autowired
	CondivisioneDAO condivisioneDAO;
	@Autowired
	UtenteDAO utenteDAO;
	@Autowired
	SincronizzazioniPendentiDAO sincDAO;
	@Autowired
	DispositivoDAO dispositivoDAO;
	
	@RequestMapping(value = "/creaCartella", method = RequestMethod.POST)
	public String creaCartellaSubmit(@RequestParam(value="nome") String nome, @RequestParam(value="pathCartella") String path, @RequestParam(value="cond") Integer cond, RedirectAttributes redirectAttrs, HttpSession session) {
		Utente utente = (Utente) session.getAttribute("utente");
		if (utente == null || utente.getEmail() == null) {
			return "index";
		}
		
		String[] pathElements = path.replace("%20", " ").split("/");
		String pathDir = new String();
		Utente owner = new Utente();
		Condivisione condivisione = new Condivisione ();
		String pathUrl = new String();
		String pathLog = new String();
		String condName = new String();
		if (cond == 1) {
			// creazione in una cartella condivisa
			condivisione = condivisioneDAO.getCondivisioneWithoutTrans((Long) session.getAttribute("cId"));
			owner = utenteDAO.getUtenteWithoutTrans(condivisione.getOwnerId());
			pathDir = condivisione.getDirPath();
			condName = pathElements[5];
			for (int i=6; i<pathElements.length; i++) {
				pathUrl += "\\" + pathElements[i];
				pathLog += "/"+pathElements[i];
			}
			pathDir += pathUrl;
		} else {
			pathDir = utente.getHome_dir();
			for (int i=5; i<pathElements.length; i++) {
				if (i==5) {
					pathUrl += pathElements[i];
				} else {
					pathUrl += "\\" + pathElements[i];
				}
			}
			pathDir += "\\Polibox\\" + pathUrl;
		}
		System.out.println(pathDir + "\\" + nome);
		File dir = new File(pathDir + "\\" + nome);
		Log log = new Log(utente.getHome_dir());
		if (!dir.isDirectory()) {
			dir.mkdir();
			//aggiorno il log file con l'azione appena compiuta
			if (cond == 1) {
				Log owner_log = new Log(owner.getHome_dir());
				String[] p = condivisione.getDirPath().split("\\\\");
				int flag=0;
				String pp = new String("http://localhost:8080/ai/home");
				for (int i=0;i<p.length;i++) {
					if (flag==1) pp += "/"+p[i];
					if (p[i].equals("Polibox")) flag=1;
				}
				//log.addLine(utente.getId(), "ND","http://localhost:8080/ai/home/"+p[p.length-1]+pathLog+"/"+nome , 0, owner.getId());
				
				Session wssession;
				SessionManager sm = SessionManager.getInstance();
				ConcurrentHashMap<Long, Session> hm;
				owner_log.addLine(owner.getId(), "ND",pp+pathLog+"/"+nome , 0, utente);					
				if ( (hm = sm.getSessionMap(owner.getId())) != null) {
					if ((wssession = hm.get(Long.parseLong("0")))!=null)
						wssession.getAsyncRemote().sendText("<div class=\"alert alert-info\" role=\"alert\"><i>"+ utente.getNome() + utente.getCognome() + "</i> ha creato la cartella <b>"+ nome +" nella cartella condivisa "+ condivisione.getDirPath().substring(condivisione.getDirPath().lastIndexOf("\\")+1) + "</b> </div>");
				}
				List<Condivisione> listCond = condivisioneDAO.getActiveCondivisioniWithoutTrans(condivisione.getDirPath());
				System.out.println("Creacartellacontroller :"+ condivisione.getDirPath());
				for(Condivisione c: listCond){
					System.out.println("Creacartellacontroller  sto ciclando utente:"+c.getUserId()+" cartella "+ c.getDirPath());
					Log l = new Log (utenteDAO.getUtenteWithoutTrans(c.getUserId()).getHome_dir());
					l.addLine(c.getUserId(), "ND","http://localhost:8080/ai/home/"+p[p.length-1]+pathLog+"/"+nome , 0, utente);
					if (c.getUserId()!=utente.getId()){
						hm = sm.getSessionMap(c.getUserId());
						System.out.println("Creacartellacontrollor: guardo se questo utente è nella mappa e ha un dispositovo 0");
						if(hm != null && (wssession = hm.get(Long.parseLong("0")))!=null){
							System.out.println("Creacartellacontroller OKKKKK");
							wssession.getAsyncRemote().sendText("<div class=\"alert alert-info\" role=\"alert\"><i>"+ utente.getNome() + utente.getCognome() + "</i> ha caricato il file <b>"+ nome +" nella cartella condivisa "+ c.getDirPath().substring(c.getDirPath().lastIndexOf("\\")+1) + "</b> </div>");
						}
					}
				}
			} else
				log.addLine(utente.getId(), "ND", path+"/"+nome, 0);
		} else {
			if (cond==1){
				for (int i=1; ;i++) {
					dir = new File(pathDir + "\\" + nome + "(" + i + ")");
					if (!dir.isDirectory()) {
						dir.mkdir();
						Log owner_log = new Log(owner.getHome_dir());
						String[] p = condivisione.getDirPath().split("\\\\");
						int flag=0;
						String pp = new String("http://localhost:8080/ai/home");
						for (int j=0;j<p.length;j++) {
							if (flag==1) pp += "/"+p[j];
							if (p[j].equals("Polibox")) flag=1;
						}
						//log.addLine(utente.getId(), "ND","http://localhost:8080/ai/home/"+p[p.length-1]+pathLog+"/"+nome , 0, owner.getId());
						
						Session wssession;
						SessionManager sm = SessionManager.getInstance();
						ConcurrentHashMap<Long, Session> hm;
						owner_log.addLine(owner.getId(), "ND",pp+pathLog+"/"+nome + "(" + i + ")" , 0, utente);					
						if ( (hm = sm.getSessionMap(owner.getId())) != null) {
							if ((wssession = hm.get(Long.parseLong("0")))!=null)
								wssession.getAsyncRemote().sendText("<div class=\"alert alert-info\" role=\"alert\"><i>"+ utente.getNome() + utente.getCognome() + "</i> ha creato la cartella <b>"+ nome + "(" + i + ")" +" nella cartella condivisa "+ condivisione.getDirPath().substring(condivisione.getDirPath().lastIndexOf("\\")+1) + "</b> </div>");
						}
						List<Condivisione> listCond = condivisioneDAO.getActiveCondivisioniWithoutTrans(condivisione.getDirPath());
						System.out.println("Creacartellacontroller :"+ condivisione.getDirPath());
						for(Condivisione c: listCond){
							System.out.println("Creacartellacontroller  sto ciclando utente:"+c.getUserId()+" cartella "+ c.getDirPath());
							Log l = new Log (utenteDAO.getUtenteWithoutTrans(c.getUserId()).getHome_dir());
							l.addLine(c.getUserId(), "ND","http://localhost:8080/ai/home/"+p[p.length-1]+pathLog+"/"+nome + "(" + i + ")" , 0, utente);
							if (c.getUserId()!=utente.getId()){
								hm = sm.getSessionMap(c.getUserId());
								if(hm != null && (wssession = hm.get(Long.parseLong("0")))!=null)
									wssession.getAsyncRemote().sendText("<div class=\"alert alert-info\" role=\"alert\"><i>"+ utente.getNome() + utente.getCognome() + "</i> ha caricato il file <b>"+ nome + "(" + i + ")" +" nella cartella condivisa "+ c.getDirPath().substring(c.getDirPath().lastIndexOf("\\")+1) + "</b> </div>");
							
							}
						}
						break;
					}
				}
			} else {
				for (int i=1; ;i++) {
					dir = new File(pathDir + "\\" + nome + "(" + i + ")");
					if (!dir.isDirectory()) {
						dir.mkdir();
						//aggiorno il log file con l'azione appena compiuta
						log.addLine(utente.getId(), "ND", path+"/"+nome + "(" + i + ")", 0);
						
						break;
					}
				}
			}
		}
		
		redirectAttrs.addFlashAttribute("utente", utente);
		redirectAttrs.addFlashAttribute("msgBool", true);
		redirectAttrs.addFlashAttribute("msg", "Cartella \"" + dir.getName() + "\" creata con successo");
		redirectAttrs.addFlashAttribute("msgClass", "success");
//		if (pathUrl.isEmpty() && cond != 1) {
//			connected(dir.getName(), utente.getId());
//			return "redirect:home";
//		}
		if(pathUrl.isEmpty())
			connected(pathDir+nome, utente.getId(),nome);
		else
			connected(pathDir+"\\"+nome, utente.getId(),pathUrl+"\\"+nome);
		if (cond == 1 && session.getAttribute("ownerCond")==null) {
			return "redirect:Home/" + condName +pathLog;
		} else if ( session.getAttribute("ownerCond")!=null) {
			return "redirect:home/" + condName + pathUrl.replace("\\", "/");
		} else
			return "redirect:home/" + pathUrl.replace("\\", "/");
	}

	@Override
	public void connected(String path,Long id,String pathRel) {
		for (Dispositivo d : dispositivoDAO.getDispositivi(id)){
			if(SessionManager.getInstance().getSessionMap(id)== null || !SessionManager.getInstance().getSessionMap(id).containsKey(d.getId())){
				System.out.println("----------------->"+id+" "+d.getId()+" "+path+" "+0);
				SincronizzazioniPendenti sinc = new SincronizzazioniPendenti(id,d.getId(),path,0);
				sincDAO.addSincronizzazioniPendenti(sinc);
			}
		}
		if (SessionManager.getInstance().getSessionMap(id)!= null) {
			for (Session s : SessionManager.getInstance().getSessionMap(id).values()){
				try {
					s.getBasicRemote().sendText("DIR:"+pathRel);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
}
