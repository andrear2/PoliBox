package it.polito.ai.polibox.controller;

import it.polito.ai.polibox.dao.CondivisioneDAO;
import it.polito.ai.polibox.dao.UtenteDAO;
import it.polito.ai.polibox.entity.Condivisione;
import it.polito.ai.polibox.entity.Utente;

import java.io.File;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EliminaController {
	@Autowired
	CondivisioneDAO condivisioneDAO;
	@Autowired
	UtenteDAO utenteDAO;
	
	@RequestMapping(value = "/elimina", method = RequestMethod.POST)
	public String eliminaFileSubmit(@RequestParam(value="nomefile") String nome, @RequestParam(value="path") String path, @RequestParam(value="cond") Integer cond, RedirectAttributes redirectAttrs, HttpSession session) {
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
			// eliminazione in una cartella condivisa
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
		double size = HomeController.directorySize(dir);
		boolean isDir = dir.isDirectory();
		String name = dir.getName();
		boolean success = EliminaController.deleteDir(dir);
		
		redirectAttrs.addFlashAttribute("utente", utente);
		redirectAttrs.addFlashAttribute("msgBool", true);
		Log log = new Log(utente.getHome_dir());
		
		if(success){
			if(isDir) {
				if(cond==1) {
					session.setAttribute("totByteFCond", (Double) session.getAttribute("totByteFCond") - size);
					Log owner_log = new Log(owner.getHome_dir());
					String[] p = condivisione.getDirPath().split("\\\\");
					int flag=0;
					String pp = new String("http://localhost:8080/ai/home");
					for (int i=0;i<p.length;i++) {
						if (flag==1) pp += "/"+p[i];
						if (p[i].equals("Polibox")) flag=1;
					}
					log.addLine(utente.getId(), "DD","http://localhost:8080/ai/home/"+p[p.length-1]+pathLog+"/"+nome , 0, owner.getId());
					owner_log.addLine(owner.getId(), "DD",pp+pathLog+"/"+nome , 0, utente.getId());
				} else {
					session.setAttribute("totByteFReg", (Double) session.getAttribute("totByteFReg") - size);
					log.addLine(utente.getId(), "DD",path+"/"+name , 0);
				}
				redirectAttrs.addFlashAttribute("msg", "Cartella " + name + " eliminata con successo");
			} else {
				if(cond==1) {
					session.setAttribute("totByteFCond", (Double) session.getAttribute("totByteFCond") - size);
					Log owner_log = new Log(owner.getHome_dir());
					String[] p = condivisione.getDirPath().split("\\\\");
					int flag=0;
					String pp = new String("http://localhost:8080/ai/home");
					for (int i=0;i<p.length;i++) {
						if (flag==1) pp += "/"+p[i];
						if (p[i].equals("Polibox")) flag=1;
					}
					log.addLine(utente.getId(), "DD","http://localhost:8080/ai/home/"+p[p.length-1]+pathLog+"/"+nome , 0, owner.getId());
					owner_log.addLine(owner.getId(), "DD",pp+pathLog+"/"+nome , 0, utente.getId());
				} else {
					session.setAttribute("totByteFReg", (Double) session.getAttribute("totByteFReg") - size);
					log.addLine(utente.getId(), "DD",path+"/"+name , 0);
				}
				redirectAttrs.addFlashAttribute("msg", "File " + name + " eliminato con successo");
			}
			redirectAttrs.addFlashAttribute("msgClass", "success");
		}else{
			if(isDir)
				redirectAttrs.addFlashAttribute("msg", "La cartella " + name + " non è stata eliminata a causa di un errore interno");
			else
				redirectAttrs.addFlashAttribute("msg", "Il file " + name + " non è stato eliminato a causa di un errore interno");
			
			redirectAttrs.addFlashAttribute("msgClass", "error");
		}
		
		if (cond == 1) {
			return "redirect:Home/" + condName +pathLog;
		} else {
			return "redirect:home/" + pathUrl.replace("\\", "/");
		}
	}
	
	public static boolean deleteDir(File dir) {
		
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i=0; i < children.length; i++) {
				boolean success = EliminaController.deleteDir(new File(dir, children[i]));
				if (!success) return false;
			}
		}
		
		return dir.delete();
	}

	
}
