package it.polito.ai.polibox.controller;

import java.io.File;

import it.polito.ai.polibox.entity.Utente;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CreaCartellaController {
	@RequestMapping(value = "/creaCartella", method = RequestMethod.POST)
	public String creaCartellaSubmit(@RequestParam(value="nome") String nome, @RequestParam(value="pathCartella") String path, RedirectAttributes redirectAttrs, HttpSession session) {
		Utente utente = (Utente) session.getAttribute("utente");
		if (utente == null || utente.getEmail() == null) {
			return "index";
		}
		
		String[] pathElements = path.split("/");
		String pathDir = utente.getHome_dir();
		String pathUrl = new String();
		for (int i=5; i<pathElements.length; i++) {
			if (i==5) {
				pathUrl += pathElements[i];
			} else {
				pathUrl += "\\" + pathElements[i];
			}
		}
		pathDir += "\\" + pathUrl;
		File dir = new File(pathDir + "\\" + nome);
		if (!dir.isDirectory()) {
			dir.mkdir();
		} else {
			for (int i=1; ;i++) {
				dir = new File(pathDir + "\\" + nome + "(" + i + ")");
				if (!dir.isDirectory()) {
					dir.mkdir();
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
		return "redirect:home/" + pathUrl;
	}
}
