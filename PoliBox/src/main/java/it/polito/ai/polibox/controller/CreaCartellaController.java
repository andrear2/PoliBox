package it.polito.ai.polibox.controller;

import java.io.File;

import it.polito.ai.polibox.dao.CondivisioneDAO;
import it.polito.ai.polibox.dao.UtenteDAO;
import it.polito.ai.polibox.entity.Condivisione;
import it.polito.ai.polibox.entity.Utente;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CreaCartellaController {
	@Autowired
	CondivisioneDAO condivisioneDAO;
	@Autowired
	UtenteDAO utenteDAO;
	
	@RequestMapping(value = "/creaCartella", method = RequestMethod.POST)
	public String creaCartellaSubmit(@RequestParam(value="nome") String nome, @RequestParam(value="pathCartella") String path, @RequestParam(value="cond") Integer cond, RedirectAttributes redirectAttrs, HttpSession session) {
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
		Log log = new Log(utente.getHome_dir());
		if (!dir.isDirectory()) {
			dir.mkdir();
			//aggiorno il log file con l'azione appena compiuta
			log.addLine(utente.getId(), "ND", path+"/"+nome, 0);
		} else {
			for (int i=1; ;i++) {
				dir = new File(pathDir + "\\" + nome + "(" + i + ")");
				if (!dir.isDirectory()) {
					dir.mkdir();
					//aggiorno il log file con l'azione appena compiuta
					log.addLine(utente.getId(), "ND", path+"/"+nome, 0);
					
					break;
				}
			}
		}
		
		redirectAttrs.addFlashAttribute("utente", utente);
		redirectAttrs.addFlashAttribute("msgBool", true);
		redirectAttrs.addFlashAttribute("msg", "Cartella \"" + dir.getName() + "\" creata con successo");
		redirectAttrs.addFlashAttribute("msgClass", "success");
		if (pathUrl.isEmpty()) {
			return "redirect:home";
		}
		if (cond == 1) {
			return "redirect:Home/" + pathUrl.replace("\\", "/");
		} else {
			return "redirect:home/" + pathUrl.replace("\\", "/");
		}
	}
}
