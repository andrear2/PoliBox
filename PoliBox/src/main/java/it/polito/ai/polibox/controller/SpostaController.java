package it.polito.ai.polibox.controller;

import java.io.File;
import java.io.IOException;

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
import org.apache.commons.io.FileUtils;

@Controller
public class SpostaController {
	@Autowired
	CondivisioneDAO condivisioneDAO;
	@Autowired
	UtenteDAO utenteDAO;
	
	@RequestMapping(value = "/sposta", method = RequestMethod.GET)
	public String spostaFile(@RequestParam(value="path") String path, @RequestParam(value="newPath") String newPath, @RequestParam(value="cond") Integer cond, RedirectAttributes redirectAttrs, HttpSession session) {
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
		if (cond == 1) {
			return "redirect:Home/" + condName +pathLog;
		} else {
			return "redirect:home/" + path.substring(0, path.lastIndexOf("/")).replace("/ai/home/", "");
		}
	}
}
