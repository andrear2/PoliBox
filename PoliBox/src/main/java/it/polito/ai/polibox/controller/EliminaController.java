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
		
		String[] pathElements = path.split("/");
		String pathDir = new String();
		if (cond == 1) {
			// creazione in una cartella condivisa
			Condivisione condivisione = condivisioneDAO.getCondivisioneWithoutTrans((Long) session.getAttribute("cId"));
			Utente owner = utenteDAO.getUtente(condivisione.getOwnerId());
			pathDir = owner.getHome_dir();
		} else {
			pathDir = utente.getHome_dir();
		}
		String pathUrl = new String();
		for (int i=5; i<pathElements.length; i++) {
			if (i==5) {
				pathUrl += pathElements[i];
			} else {
				pathUrl += "\\" + pathElements[i];
			}
		}
		pathDir += "\\Polibox\\" + pathUrl;
		File dir = new File(pathDir + "\\" + nome);
		boolean isDir = dir.isDirectory();
		String name = dir.getName();
		boolean success = EliminaController.deleteDir(dir);
		
		redirectAttrs.addFlashAttribute("utente", utente);
		redirectAttrs.addFlashAttribute("msgBool", true);
		Log log = new Log(utente.getHome_dir());
		
		if(success){
			if(isDir) {
				redirectAttrs.addFlashAttribute("msg", "Cartella " + name + " eliminata con successo");
				log.addLine(utente.getId(), "DD",path+"/"+name , 0);
			} else {
				redirectAttrs.addFlashAttribute("msg", "File " + name + " eliminato con successo");
				log.addLine(utente.getId(), "DF", path+"/"+name, 0);
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
			return "redirect:Home/" + pathUrl.replace("\\", "/");
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
