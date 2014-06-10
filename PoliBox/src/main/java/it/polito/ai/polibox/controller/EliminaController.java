package it.polito.ai.polibox.controller;

import it.polito.ai.polibox.entity.Utente;

import java.io.File;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EliminaController {
	@RequestMapping(value = "/elimina", method = RequestMethod.POST)
	public String eliminaFileSubmit(@RequestParam(value="nomefile") String nome, @RequestParam(value="path") String path, RedirectAttributes redirectAttrs, HttpSession session) {
		Utente utente = (Utente) session.getAttribute("utente");
		if (utente == null || utente.getEmail() == null) {
			return "index";
		}
		
		String[] pathElements = path.split("/");
		String pathDir = utente.getHome_dir();
		String pathUrl = new String();
		for (int i=5; i<pathElements.length; i++) {
				pathUrl += "\\" + pathElements[i];
		}
		pathDir += "\\" + pathUrl;
		File dir = new File(pathDir + "\\" + nome);
		boolean isDir = dir.isDirectory();
		String name = dir.getName();
		boolean success = EliminaController.deleteDir(dir);
		
		redirectAttrs.addFlashAttribute("utente", utente);
		redirectAttrs.addFlashAttribute("msgBool", true);
		
		if(success){
			if(isDir)
				redirectAttrs.addFlashAttribute("msg", "Cartella " + name + " eliminata con successo");
			else
				redirectAttrs.addFlashAttribute("msg", "File " + name + " eliminato con successo");
			
			redirectAttrs.addFlashAttribute("msgClass", "success");
		}else{
			if(isDir)
				redirectAttrs.addFlashAttribute("msg", "La cartella " + name + " non è stata eliminata a causa di un errore interno");
			else
				redirectAttrs.addFlashAttribute("msg", "Il file " + name + " non è stato eliminato a causa di un errore interno");
			
			redirectAttrs.addFlashAttribute("msgClass", "error");
		}
		
		return "redirect:home" + pathUrl;
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
