package it.polito.ai.polibox.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import it.polito.ai.polibox.dao.CondivisioneDAO;
import it.polito.ai.polibox.dao.UtenteDAO;
import it.polito.ai.polibox.entity.Condivisione;
import it.polito.ai.polibox.entity.SessionManager;
import it.polito.ai.polibox.entity.Utente;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.apache.commons.io.FileUtils;

@Controller
public class SpostaController {
	@Autowired
	CondivisioneDAO condivisioneDAO;
	@Autowired
	UtenteDAO utenteDAO;
	
	@RequestMapping(value = "/sposta", method = RequestMethod.GET)
	public String spostaFile(@RequestParam(value="path") String path, @RequestParam(value="newPath") String newPath, @RequestParam(value="cond") Integer cond, @RequestParam(value="cId") Long cId, RedirectAttributes redirectAttrs, HttpSession session) {
		Utente utente = (Utente) session.getAttribute("utente");
		if (utente == null || utente.getEmail() == null) {
			return "index";
		}
		String[] pathElements = path.replace("%20", " ").split("/");
		String[] newPathElements = newPath.replace("%20", " ").split("/");
		String pathDir = new String();
		String newPathDir = new String();
		Utente owner = new Utente();
		Condivisione condivisione = new Condivisione ();
		String pathUrl = new String();
		String newPathUrl = new String();
		String pathLog = new String();
		String newPathLog = new String();
		String condName = new String();
		if (cond == 1) {
			// spostamento all'interno di cartella condivisa di cui utente NON è proprietario
			
			condivisione = condivisioneDAO.getCondivisioneWithoutTrans((Long) session.getAttribute("cId"));
			pathDir = condivisione.getDirPath();
			condName = condivisione.getDirPath().substring(condivisione.getDirPath().lastIndexOf("\\")+1);
			owner = utenteDAO.getUtenteWithoutTrans(condivisione.getOwnerId());
			
			for (int i=3; i < pathElements.length; i++) {
				pathUrl += "\\" + pathElements[i];
				if(i != pathElements.length-1)
					pathLog += "/"+pathElements[i];
			}
			pathDir += pathUrl;
			
			redirectAttrs.addFlashAttribute("utente", utente);
			redirectAttrs.addFlashAttribute("msgBool", true);
			
			if(condivisione.getReadOnly()){
				redirectAttrs.addFlashAttribute("msg", "Spostamento non consentito: cartella"+ condName +" è condivisa in sola lettura");
				redirectAttrs.addFlashAttribute("msgClass", "error");
				return "redirect:Home/" + condName + pathLog;
			}
			
			newPathDir = condivisione.getDirPath();
			for (int i=3; i < newPathElements.length; i++) {
				newPathUrl += "\\" + newPathElements[i];
				newPathLog += "/"+pathElements[i];
			}
			newPathDir += newPathUrl;
			
			File dir = new File(pathDir);
			File newDir = new File(newPathDir);
			boolean isDir = dir.isDirectory();
			
			try {
				if (isDir) {
					FileUtils.copyDirectoryToDirectory(dir, newDir);
					FileUtils.deleteDirectory(dir);
					redirectAttrs.addFlashAttribute("msg", "La cartella " + path.substring(path.lastIndexOf("/") + 1) + " è stata spostata in " + newPath.substring(newPath.lastIndexOf("/") + 1));
				
					Session wssession;
					SessionManager sm = SessionManager.getInstance();
					Log owner_log = new Log(owner.getHome_dir());
					ConcurrentHashMap<Long, Session> hm;
					if ( (hm = sm.getSessionMap(owner.getId())) != null) {
						//owner_log.addLine(owner.getId(), "MD", pathLog + pathElements[pathElements.length-1], newPathLog, 1, utente);					
						if ((wssession = hm.get(Long.parseLong("0"))) != null)
							wssession.getAsyncRemote().sendText("<div class=\"alert alert-info\" role=\"alert\"><i>"+ utente.getNome() + utente.getCognome() + "</i> ha spostato la cartella <b>"+ pathLog + pathElements[pathElements.length-1] +" nella cartella "+ newPathLog + "</b> </div>");
					}
					List<Condivisione> listCond = condivisioneDAO.getActiveCondivisioniWithoutTrans(condivisione.getDirPath());
					for(Condivisione c: listCond){
						Log log = new Log(utenteDAO.getUtenteWithoutTrans(c.getUserId()).getHome_dir());
						//log.addLine(c.getId(), "DD","http://localhost:8080/ai/home/"+p[p.length-1]+pathLog+"/"+nome , 0, utente);
						if (c.getUserId() != utente.getId()){
							hm = sm.getSessionMap(c.getUserId());
							if(hm != null && (wssession = hm.get(Long.parseLong("0"))) != null)
								wssession.getAsyncRemote().sendText("<div class=\"alert alert-info\" role=\"alert\"><i>"+ utente.getNome() + utente.getCognome() + "</i> ha spostato la cartella <b>"+ pathLog + pathElements[pathElements.length-1] +" nella cartella "+ newPathLog + "</b></div>");
						}
					}
					
				} else {
					FileUtils.copyFileToDirectory(dir, newDir);
					dir.delete();
					redirectAttrs.addFlashAttribute("msg", "Il file " + path.substring(path.lastIndexOf("/") + 1) + " è stato spostato in " + newPath.substring(newPath.lastIndexOf("/") + 1));
				
					Session wssession;
					SessionManager sm = SessionManager.getInstance();
					Log owner_log = new Log(owner.getHome_dir());
					ConcurrentHashMap<Long, Session> hm;
					if ( (hm = sm.getSessionMap(owner.getId())) != null) {
						//owner_log.addLine(owner.getId(), "MD", pathLog + pathElements[pathElements.length-1], newPathLog, 1, utente);					
						if ((wssession = hm.get(Long.parseLong("0"))) != null)
							wssession.getAsyncRemote().sendText("<div class=\"alert alert-info\" role=\"alert\"><i>"+ utente.getNome() + utente.getCognome() + "</i> ha spostato il file <b>"+ pathLog + pathElements[pathElements.length-1] +" nella cartella "+ newPathLog + "</b> </div>");
					}
					List<Condivisione> listCond = condivisioneDAO.getActiveCondivisioniWithoutTrans(condivisione.getDirPath());
					for(Condivisione c: listCond){
						Log log = new Log(utenteDAO.getUtenteWithoutTrans(c.getUserId()).getHome_dir());
						//log.addLine(c.getId(), "DD","http://localhost:8080/ai/home/"+p[p.length-1]+pathLog+"/"+nome , 0, utente);
						if (c.getUserId() != utente.getId()){
							hm = sm.getSessionMap(c.getUserId());
							if(hm != null && (wssession = hm.get(Long.parseLong("0"))) != null)
								wssession.getAsyncRemote().sendText("<div class=\"alert alert-info\" role=\"alert\"><i>"+ utente.getNome() + utente.getCognome() + "</i> ha spostato il file <b>"+ pathLog + pathElements[pathElements.length-1] +" nella cartella "+ newPathLog + "</b></div>");
						}
					}
					
				}
				redirectAttrs.addFlashAttribute("msgClass", "success");
				
				return "redirect:Home/" + condName + newPathLog;
				
			} catch (IOException e) {
				e.printStackTrace();
				redirectAttrs.addFlashAttribute("msg", "Errore durante lo spostamento");
				redirectAttrs.addFlashAttribute("msgClass", "error");
				return "redirect:Home/" + condName + pathLog;
			}		
			
		} else if(cond == 2){
			// sposto file/cartella NON condivisa dentro cartella condivisa di cui non sono proprietario
			
			
			pathDir = utente.getHome_dir();					
			for (int i=2; i < pathElements.length; i++) {
				pathUrl += "\\" + pathElements[i];
				if(i != pathElements.length-1)
					pathLog += "/"+pathElements[i];
			}
			pathDir += pathUrl;
			
			
			condivisione = condivisioneDAO.getCondivisione(cId);
			owner = (Utente) utenteDAO.getUtenteWithoutTrans(condivisione.getOwnerId());
			condName = condivisione.getDirPath().substring(condivisione.getDirPath().lastIndexOf("\\")+1);
			
			newPathDir = condivisione.getDirPath();
			for(int i = 2; i < newPathElements.length; i++){
				newPathUrl += "\\" + newPathElements[i];
				newPathLog += "/" + newPathElements[i];
			}
			newPathDir += newPathUrl;
			
						
			redirectAttrs.addFlashAttribute("utente", utente);
			redirectAttrs.addFlashAttribute("msgBool", true);
						
			if(condivisione.getReadOnly()){
				redirectAttrs.addFlashAttribute("msg", "Spostamento non consentito: cartella"+ condName +" è condivisa in sola lettura");
				redirectAttrs.addFlashAttribute("msgClass", "error");
				return "redirect:home/" + pathLog;
			}
						
			File dir = new File(pathDir);
			File newDir = new File(newPathDir);
			boolean isDir = dir.isDirectory();
			
			try {
				if (isDir) {
					FileUtils.copyDirectoryToDirectory(dir, newDir);
					FileUtils.deleteDirectory(dir);
					redirectAttrs.addFlashAttribute("msg", "La cartella " + path + " è stata spostata in " + newPath);
				
					Session wssession;
					SessionManager sm = SessionManager.getInstance();
					Log owner_log = new Log(owner.getHome_dir());
					ConcurrentHashMap<Long, Session> hm;
					if ( (hm = sm.getSessionMap(owner.getId())) != null) {
						//owner_log.addLine(owner.getId(), "MD", pathLog + pathElements[pathElements.length-1], newPathLog, 1, utente);					
						if ((wssession = hm.get(Long.parseLong("0"))) != null)
							wssession.getAsyncRemote().sendText("<div class=\"alert alert-info\" role=\"alert\"><i>"+ utente.getNome() + utente.getCognome() + "</i> ha aggiunto la cartella <b>"+ pathElements[pathElements.length-1] +" nella cartella condivisa"+ condName + "</b> </div>");
					}
					List<Condivisione> listCond = condivisioneDAO.getActiveCondivisioniWithoutTrans(condivisione.getDirPath());
					for(Condivisione c: listCond){
						Log log = new Log(utenteDAO.getUtenteWithoutTrans(c.getUserId()).getHome_dir());
						//log.addLine(c.getId(), "DD","http://localhost:8080/ai/home/"+p[p.length-1]+pathLog+"/"+nome , 0, utente);
						if (c.getUserId() != utente.getId()){
							hm = sm.getSessionMap(c.getUserId());
							if(hm != null && (wssession = hm.get(Long.parseLong("0"))) != null)
								wssession.getAsyncRemote().sendText("<div class=\"alert alert-info\" role=\"alert\"><i>"+ utente.getNome() + utente.getCognome() + "</i> ha aggiunto la cartella <b>"+ pathElements[pathElements.length-1] +" nella cartella condivisa "+ condName + "</b></div>");
						}
					}
					
				} else {
					FileUtils.copyFileToDirectory(dir, newDir);
					dir.delete();
					redirectAttrs.addFlashAttribute("msg", "Il file " + path.substring(path.lastIndexOf("/") + 1) + " è stato spostato in " + newPath.substring(newPath.lastIndexOf("/") + 1));
				
					Session wssession;
					SessionManager sm = SessionManager.getInstance();
					Log owner_log = new Log(owner.getHome_dir());
					ConcurrentHashMap<Long, Session> hm;
					if ( (hm = sm.getSessionMap(owner.getId())) != null) {
						//owner_log.addLine(owner.getId(), "MD", pathLog + pathElements[pathElements.length-1], newPathLog, 1, utente);					
						if ((wssession = hm.get(Long.parseLong("0"))) != null)
							wssession.getAsyncRemote().sendText("<div class=\"alert alert-info\" role=\"alert\"><i>"+ utente.getNome() + utente.getCognome() + "</i> ha spostato il file <b>"+ pathElements[pathElements.length-1] +" nella cartella condivisa "+ condName + "</b> </div>");
					}
					List<Condivisione> listCond = condivisioneDAO.getActiveCondivisioniWithoutTrans(condivisione.getDirPath());
					for(Condivisione c: listCond){
						Log log = new Log(utenteDAO.getUtenteWithoutTrans(c.getUserId()).getHome_dir());
						//log.addLine(c.getId(), "DD","http://localhost:8080/ai/home/"+p[p.length-1]+pathLog+"/"+nome , 0, utente);
						if (c.getUserId() != utente.getId()){
							hm = sm.getSessionMap(c.getUserId());
							if(hm != null && (wssession = hm.get(Long.parseLong("0"))) != null)
								wssession.getAsyncRemote().sendText("<div class=\"alert alert-info\" role=\"alert\"><i>"+ utente.getNome() + utente.getCognome() + "</i> ha spostato il file <b>"+ pathElements[pathElements.length-1] +" nella cartella condivisa "+ condName + "</b></div>");
						}
					}
					
				}
				redirectAttrs.addFlashAttribute("msgClass", "success");
				
				return "redirect:Home/" + newPathLog;
				
			} catch (IOException e) {
				e.printStackTrace();
				redirectAttrs.addFlashAttribute("msg", "Errore durante lo spostamento");
				redirectAttrs.addFlashAttribute("msgClass", "error");
				return "redirect:home/" + pathLog;
			}				
					
		} else if(cond == 3){
			// sposto file/cartella NON condivisa dentro cartella condivisa di cui sono proprietario
			
			pathDir = utente.getHome_dir();					
			for (int i=2; i < pathElements.length; i++) {
				pathUrl += "\\" + pathElements[i];
				if(i != pathElements.length-1)
					pathLog += "/"+pathElements[i];
			}
			pathDir += pathUrl;
			
			newPathDir = utente.getHome_dir();
			for (int i=2; i < newPathElements.length; i++) {
				newPathUrl += "\\" + pathElements[i];
				newPathLog += "/"+pathElements[i];
			}
			newPathDir += newPathUrl;
			
			condName = newPathDir.substring(newPathDir.lastIndexOf("\\") + 1);
			
			File dir = new File(pathDir);
			File newDir = new File(newPathDir);
			boolean isDir = dir.isDirectory();
			
			redirectAttrs.addFlashAttribute("utente", utente);
			redirectAttrs.addFlashAttribute("msgBool", true);
			
			try {
				if (isDir) {
					FileUtils.copyDirectoryToDirectory(dir, newDir);
					FileUtils.deleteDirectory(dir);
					redirectAttrs.addFlashAttribute("msg", "La cartella " + path.substring(path.lastIndexOf("/")+1) + " è stata spostata in " + newPath.substring(newPath.lastIndexOf("/")+1));
				
					Log owner_log = new Log(utente.getHome_dir());
					//owner_log.addLine(owner.getId(), "MD", pathLog + pathElements[pathElements.length-1], newPathLog, 1, utente);					
						
					List<Condivisione> listCond = condivisioneDAO.getActiveCondivisioniWithoutTrans(newPathDir);
					Session wssession;
					SessionManager sm = SessionManager.getInstance();
					ConcurrentHashMap<Long, Session> hm;
					if(listCond != null){
						for(Condivisione c: listCond){
							Log log = new Log(utenteDAO.getUtenteWithoutTrans(c.getUserId()).getHome_dir());
							//log.addLine(c.getId(), "DD","http://localhost:8080/ai/home/"+p[p.length-1]+pathLog+"/"+nome , 0, utente);
							hm = sm.getSessionMap(c.getUserId());
								if(hm != null && (wssession = hm.get(Long.parseLong("0"))) != null)
									wssession.getAsyncRemote().sendText("<div class=\"alert alert-info\" role=\"alert\"><i>"+ utente.getNome() + utente.getCognome() + "</i> ha aggiunto la cartella <b>"+ pathElements[pathElements.length-1] +" nella cartella condivisa "+ condName + "</b></div>");
						}
					}
					
				} else {
					FileUtils.copyFileToDirectory(dir, newDir);
					dir.delete();
					redirectAttrs.addFlashAttribute("msg", "Il file " + path.substring(path.lastIndexOf("/") + 1) + " è stato spostato in " + newPath.substring(newPath.lastIndexOf("/") + 1));
				
					
					Log owner_log = new Log(utente.getHome_dir());
					//owner_log.addLine(owner.getId(), "MD", pathLog + pathElements[pathElements.length-1], newPathLog, 1, utente);					
					
					List<Condivisione> listCond = condivisioneDAO.getActiveCondivisioniWithoutTrans(newPathDir);
					Session wssession;
					SessionManager sm = SessionManager.getInstance();
					ConcurrentHashMap<Long, Session> hm;
					if(listCond != null){
						for(Condivisione c: listCond){
							Log log = new Log(utenteDAO.getUtenteWithoutTrans(c.getUserId()).getHome_dir());
							//log.addLine(c.getId(), "DD","http://localhost:8080/ai/home/"+p[p.length-1]+pathLog+"/"+nome , 0, utente);
							hm = sm.getSessionMap(c.getUserId());
							if(hm != null && (wssession = hm.get(Long.parseLong("0"))) != null)
								wssession.getAsyncRemote().sendText("<div class=\"alert alert-info\" role=\"alert\"><i>"+ utente.getNome() + utente.getCognome() + "</i> ha spostato il file <b>"+ pathElements[pathElements.length-1] +" nella cartella condivisa "+ condName + "</b></div>");		
						}
					}
					
				}
				redirectAttrs.addFlashAttribute("msgClass", "success");
				
				return "redirect:home/" + newPathLog;
				
			} catch (IOException e) {
				e.printStackTrace();
				redirectAttrs.addFlashAttribute("msg", "Errore durante lo spostamento");
				redirectAttrs.addFlashAttribute("msgClass", "error");
				return "redirect:home/" + pathLog;
			}	
			
		} else if(cond == 4) {
			// spostamento interno a cartella condivisa di cui sono il proprietario
			
			pathDir = utente.getHome_dir();					
			for (int i=2; i < pathElements.length; i++) {
				pathUrl += "\\" + pathElements[i];
				if(i != pathElements.length-1)
					pathLog += "/"+pathElements[i];
			}
			pathDir += pathUrl;
			
			newPathDir = utente.getHome_dir();
			for (int i=2; i < newPathElements.length; i++) {
				newPathUrl += "\\" + pathElements[i];
				newPathLog += "/"+pathElements[i];
			}
			newPathDir += newPathUrl;
			
			List<Condivisione> activeCond = null;
			String pathCond = utente.getHome_dir();
			for (int i=2; i < pathElements.length; i++) {
				pathCond += "\\" + pathElements[i];
				if((activeCond = condivisioneDAO.getActiveCondivisioniWithoutTrans(pathCond)) != null){
					condName = pathElements[i];
					break;
				}
			}
			
			
			File dir = new File(pathDir);
			File newDir = new File(newPathDir);
			boolean isDir = dir.isDirectory();
			
			redirectAttrs.addFlashAttribute("utente", utente);
			redirectAttrs.addFlashAttribute("msgBool", true);
			
			try {
				if (isDir) {
					FileUtils.copyDirectoryToDirectory(dir, newDir);
					FileUtils.deleteDirectory(dir);
					redirectAttrs.addFlashAttribute("msg", "La cartella " + path.substring(path.lastIndexOf("/")+1) + " è stata spostata in " + newPath.substring(newPath.lastIndexOf("/")+1));
				
					Log owner_log = new Log(utente.getHome_dir());
					//owner_log.addLine(owner.getId(), "MD", pathLog + pathElements[pathElements.length-1], newPathLog, 1, utente);					
						
					Session wssession;
					SessionManager sm = SessionManager.getInstance();
					ConcurrentHashMap<Long, Session> hm;
					if(activeCond != null){
						for(Condivisione c: activeCond){
							Log log = new Log(utenteDAO.getUtenteWithoutTrans(c.getUserId()).getHome_dir());
							//log.addLine(c.getId(), "DD","http://localhost:8080/ai/home/"+p[p.length-1]+pathLog+"/"+nome , 0, utente);
							hm = sm.getSessionMap(c.getUserId());
							if(hm != null && (wssession = hm.get(Long.parseLong("0"))) != null)
								wssession.getAsyncRemote().sendText("<div class=\"alert alert-info\" role=\"alert\"><i>"+ utente.getNome() + utente.getCognome() + "</i> ha aggiunto la cartella <b>"+ pathElements[pathElements.length-1] +" nella cartella condivisa "+ condName + "</b></div>");
						}
					}
					
				} else {
					FileUtils.copyFileToDirectory(dir, newDir);
					dir.delete();
					redirectAttrs.addFlashAttribute("msg", "Il file " + path.substring(path.lastIndexOf("/") + 1) + " è stato spostato in " + newPath.substring(newPath.lastIndexOf("/") + 1));
				
					
					Log owner_log = new Log(utente.getHome_dir());
					//owner_log.addLine(owner.getId(), "MD", pathLog + pathElements[pathElements.length-1], newPathLog, 1, utente);					
					
					Session wssession;
					SessionManager sm = SessionManager.getInstance();
					ConcurrentHashMap<Long, Session> hm;
					if(activeCond != null){
						for(Condivisione c: activeCond){
							Log log = new Log(utenteDAO.getUtenteWithoutTrans(c.getUserId()).getHome_dir());
							//log.addLine(c.getId(), "DD","http://localhost:8080/ai/home/"+p[p.length-1]+pathLog+"/"+nome , 0, utente);
							hm = sm.getSessionMap(c.getUserId());
							if(hm != null && (wssession = hm.get(Long.parseLong("0"))) != null)
								wssession.getAsyncRemote().sendText("<div class=\"alert alert-info\" role=\"alert\"><i>"+ utente.getNome() + utente.getCognome() + "</i> ha spostato il file <b>"+ pathElements[pathElements.length-1] +" nella cartella condivisa "+ condName + "</b></div>");		
						}
					}
					
				}
				redirectAttrs.addFlashAttribute("msgClass", "success");
				
				return "redirect:home/" + newPathLog;
				
			} catch (IOException e) {
				e.printStackTrace();
				redirectAttrs.addFlashAttribute("msg", "Errore durante lo spostamento");
				redirectAttrs.addFlashAttribute("msgClass", "error");
				return "redirect:home/" + pathLog;
			}	
			
			
			
		} else if(cond == 5){
			// sposto cartella condivisa di cui sono proprietario dentro cartella non condivisa
			
			pathDir = utente.getHome_dir();					
			for (int i=2; i < pathElements.length; i++) {
				pathUrl += "\\" + pathElements[i];
				if(i != pathElements.length-1)
					pathLog += "/"+pathElements[i];
			}
			pathDir += pathUrl;
			
			newPathDir = utente.getHome_dir();
			for (int i=2; i < newPathElements.length; i++) {
				newPathUrl += "\\" + pathElements[i];
				newPathLog += "/"+pathElements[i];
			}
			newPathDir += newPathUrl;
			
			List<Condivisione> listCond = condivisioneDAO.getCondivisioni(pathDir);
			System.out.println("-->"+listCond.toString());
			if(listCond != null){
				for(Condivisione c: listCond){
					System.out.println("-->"+newPathDir + "\\" + pathElements[pathElements.length-1]);
					c.setDirPath(newPathDir + "\\" + pathElements[pathElements.length-1]);
					condivisioneDAO.updateCondivisione(c);
				}
			}
				
			File dir = new File(pathDir);
			File newDir = new File(newPathDir);
			
			redirectAttrs.addFlashAttribute("utente", utente);
			redirectAttrs.addFlashAttribute("msgBool", true);
			
			try {
				FileUtils.copyDirectoryToDirectory(dir, newDir);
				FileUtils.deleteDirectory(dir);
				redirectAttrs.addFlashAttribute("msg", "La cartella " + path.substring(path.lastIndexOf("/")+1) + " è stata spostata in " + newPath.substring(newPath.lastIndexOf("/")+1));
				
				Log owner_log = new Log(utente.getHome_dir());
				//owner_log.addLine(owner.getId(), "MD", pathLog + pathElements[pathElements.length-1], newPathLog, 1, utente);					
						
				
				redirectAttrs.addFlashAttribute("msgClass", "success");
				
				return "redirect:home/" + newPathLog;
				
			} catch (IOException e) {
				e.printStackTrace();
				redirectAttrs.addFlashAttribute("msg", "Errore durante lo spostamento");
				redirectAttrs.addFlashAttribute("msgClass", "error");
				return "redirect:home/" + pathLog;
			}	
			
			
		} else {
			pathDir = utente.getHome_dir();
			for (int i=3; i<pathElements.length; i++) {
				if (i==3) {
					pathUrl += pathElements[i];
				} else {
					pathUrl += "\\" + pathElements[i];
				}
			}
			pathDir += "\\Polibox\\" + pathUrl;
			newPathDir = utente.getHome_dir();
			for (int i=3; i<newPathElements.length; i++) {
				if (i==3) {
					newPathUrl += newPathElements[i];
				} else {
					newPathUrl += "\\" + newPathElements[i];
				}
			}
			newPathDir += "\\Polibox\\" + newPathUrl;
			
			File dir = new File(pathDir);
			File newDir = new File(newPathDir);
			boolean isDir = dir.isDirectory();
			
			redirectAttrs.addFlashAttribute("utente", utente);
			redirectAttrs.addFlashAttribute("msgBool", true);
			
			try {
				if (isDir) {
					FileUtils.copyDirectoryToDirectory(dir, newDir);
					FileUtils.deleteDirectory(dir);
					redirectAttrs.addFlashAttribute("msg", "La cartella " + path.substring(path.lastIndexOf("/") + 1) + " è stata spostata in " + newPath.substring(newPath.lastIndexOf("/") + 1));
				} else {
					FileUtils.copyFileToDirectory(dir, newDir);
					dir.delete();
					redirectAttrs.addFlashAttribute("msg", "Il file " + path.substring(path.lastIndexOf("/") + 1) + " è stato spostato in " + newPath.substring(newPath.lastIndexOf("/") + 1));
				}
				redirectAttrs.addFlashAttribute("msgClass", "success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				redirectAttrs.addFlashAttribute("msg", "Errore durante lo spostamento");
				redirectAttrs.addFlashAttribute("msgClass", "error");
			}
		}

		
		if (cond == 1) {
			return "redirect:Home/" + condName +pathLog;
		} else {
			return "redirect:home/" + path.substring(0, path.lastIndexOf("/") + 1).replace("/ai/home/", "");
		}
	}
}
