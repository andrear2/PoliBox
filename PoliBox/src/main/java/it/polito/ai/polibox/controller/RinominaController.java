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
		
		if (dir.renameTo(newDir)) {
			if (isDir) {
				redirectAttrs.addFlashAttribute("msg", "Cartella " + name + " rinominata in " + newName);
			} else {
				redirectAttrs.addFlashAttribute("msg", "File " + name + " rinominato in " + newName);
			}
			redirectAttrs.addFlashAttribute("msgClass", "success");
		} else {
			if(isDir)
				redirectAttrs.addFlashAttribute("msg", "La cartella " + name + " non è stata rinominata a causa di un errore interno");
			else
				redirectAttrs.addFlashAttribute("msg", "Il file " + name + " non è stato rinominato a causa di un errore interno");
			
			redirectAttrs.addFlashAttribute("msgClass", "error");
		}
		
		if (cond == 1) {
			return "redirect:Home/" + condName +pathLog;
		} else {
			return "redirect:home/" + pathUrl.replace("\\", "/");
		}
	}
}
