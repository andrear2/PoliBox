package it.polito.ai.polibox.controller;

import it.polito.ai.polibox.dao.CondivisioneDAO;
import it.polito.ai.polibox.dao.UtenteDAO;
import it.polito.ai.polibox.entity.Condivisione;
import it.polito.ai.polibox.entity.SessionManager;
import it.polito.ai.polibox.entity.Utente;

import java.io.File;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RinominaController {
	@Autowired
	CondivisioneDAO condivisioneDAO;
	@Autowired
	UtenteDAO utenteDAO;
	
	@RequestMapping(value = "/rinomina", method = RequestMethod.POST)
	public String rinominaSubmit(@RequestParam(value="newName") String newName, @RequestParam(value="nomefile") String nome, @RequestParam(value="path") String path, @RequestParam(value="cond") Integer cond, RedirectAttributes redirectAttrs, HttpSession session) {
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
		File dir = new File(pathDir + "\\" + nome);
		boolean isDir = dir.isDirectory();
		String name = dir.getName();
		File newDir = new File(pathDir + "\\" + newName);
		System.out.println("----> " + pathDir + "\\" + nome);
		System.out.println("----> " + pathDir + "\\" + newName);
		
		redirectAttrs.addFlashAttribute("utente", utente);
		redirectAttrs.addFlashAttribute("msgBool", true);
		String logType = new String ();
		String resourceType = new String();
		if (dir.renameTo(newDir)) {
			if (isDir) {
				logType = "D";
				resourceType ="la cartella";
				redirectAttrs.addFlashAttribute("msg", "Cartella " + name + " rinominata in " + newName);
			} else {
				logType = "F";
				resourceType ="il file";
				redirectAttrs.addFlashAttribute("msg", "File " + name + " rinominato in " + newName);
			}
			if (cond==1) {
				Log owner_log = new Log(owner.getHome_dir());
				String[] p = condivisione.getDirPath().split("\\\\");
				int flag=0;
				String pp = new String("http://localhost:8080/ai/home");
				for (int i=0;i<p.length;i++) {
					if (flag==1) pp += "/"+p[i];
					if (p[i].equals("Polibox")) flag=1;
				}
				Session wssession;
				SessionManager sm = SessionManager.getInstance();
				ConcurrentHashMap<Long, Session> hm;
				owner_log.addLine(owner.getId(), "D"+logType ,pp+pathLog+"/"+nome , 0, utente);	
				owner_log.addLine(owner.getId(), "N"+logType ,pp+pathLog+"/"+newName , 0, utente);		
				if ( (hm = sm.getSessionMap(owner.getId())) != null) {
					if ((wssession = hm.get(Long.parseLong("0")))!=null)
						wssession.getAsyncRemote().sendText("<div class=\"alert alert-info\" role=\"alert\"><i>"+ utente.getNome() + utente.getCognome() + "</i> ha rinominato "+ resourceType+" <b>"+ nome +"in "+newName+ " nella cartella condivisa "+ condivisione.getDirPath().substring(condivisione.getDirPath().lastIndexOf("\\")+1) + "</b> </div>");
				}
				List<Condivisione> listCond = condivisioneDAO.getActiveCondivisioniWithoutTrans(condivisione.getDirPath());
				for(Condivisione c: listCond){
					Log l = new Log (utenteDAO.getUtenteWithoutTrans(c.getUserId()).getHome_dir());
					l.addLine(c.getUserId(), "D"+logType,"http://localhost:8080/ai/home/"+p[p.length-1]+pathLog+"/"+nome , 0, utente);
					l.addLine(c.getUserId(), "N"+logType,"http://localhost:8080/ai/home/"+p[p.length-1]+pathLog+"/"+newName , 0, utente);
					if (c.getUserId()!=utente.getId()){
						hm = sm.getSessionMap(c.getUserId());
						if(hm != null && (wssession = hm.get(Long.parseLong("0")))!=null){
							wssession.getAsyncRemote().sendText("<div class=\"alert alert-info\" role=\"alert\"><i>"+ utente.getNome() + utente.getCognome() + "</i> ha rinominato "+ resourceType+" <b>"+ nome +"in "+newName+ " nella cartella condivisa "+ c.getDirPath().substring(c.getDirPath().lastIndexOf("\\")+1) + "</b> </div>");
						}
					}
				}
			} else {
				Log l = new Log (utente.getHome_dir());
				l.addLine(utente.getId(), "D"+logType,"http://localhost:8080/ai/home/"+path+"/"+nome , 0);
				l.addLine(utente.getId(), "N"+logType,"http://localhost:8080/ai/home/"+path+"/"+newName , 0);
				
			}
			
			redirectAttrs.addFlashAttribute("msgClass", "success");
		} else {
			if(isDir)
				redirectAttrs.addFlashAttribute("msg", "La cartella " + name + " non è stata rinominata a causa di un errore interno");
			else
				redirectAttrs.addFlashAttribute("msg", "Il file " + name + " non è stato rinominato a causa di un errore interno");
			
			redirectAttrs.addFlashAttribute("msgClass", "error");
		}
		
		if (cond == 1 && session.getAttribute("ownerCond")==null) {
			return "redirect:Home/" + condName +pathLog;
		}  else if ( session.getAttribute("ownerCond")!=null) {
			return "redirect:home/" + condName + pathUrl.replace("\\", "/");
		} else {
			return "redirect:home/" + pathUrl.replace("\\", "/");
		}
	}
}
