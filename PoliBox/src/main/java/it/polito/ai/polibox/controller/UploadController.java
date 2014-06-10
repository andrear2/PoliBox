package it.polito.ai.polibox.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import it.polito.ai.polibox.entity.Utente;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UploadController {
	@RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
	public String fileUploadSubmit(@RequestParam(value="files") List<MultipartFile> uploadedFiles, @RequestParam(value="pathFile") String path, RedirectAttributes redirectAttrs, HttpSession session) {
		Utente utente = (Utente) session.getAttribute("utente");
		if (utente == null || utente.getEmail() == null) {
			return "index";
		}
		
		System.out.println(path);
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
		List<String> fileNames = new ArrayList<String>();
		  
		if (uploadedFiles == null || uploadedFiles.size() == 0 || uploadedFiles.get(0).getOriginalFilename() == "") {
			redirectAttrs.addFlashAttribute("utente", utente);
			redirectAttrs.addFlashAttribute("msgBool", true);
			redirectAttrs.addFlashAttribute("msg", "Nessun file selezionato");
			redirectAttrs.addFlashAttribute("msgClass", "error");
			if (pathUrl.isEmpty()) {
				return "redirect:home";
			}
			return "redirect:home/" + pathUrl;
		}
		
		for (MultipartFile file: uploadedFiles) {
			String fileName = file.getOriginalFilename();
			fileNames.add(fileName);
			File dest = new File(pathDir + "\\" + fileName);
			try {
				file.transferTo(dest);
			} catch (IllegalStateException ise) {
				ise.printStackTrace();
				redirectAttrs.addFlashAttribute("utente", utente);
				redirectAttrs.addFlashAttribute("msgBool", true);
				redirectAttrs.addFlashAttribute("msg", "Errore nel caricamento del file");
				redirectAttrs.addFlashAttribute("msgClass", "error");
				if (pathUrl.isEmpty()) {
					return "redirect:home";
				}
				return "redirect:home/" + pathUrl;
			} catch (IOException ioe) {
				ioe.printStackTrace();
				redirectAttrs.addFlashAttribute("utente", utente);
				redirectAttrs.addFlashAttribute("msgBool", true);
				redirectAttrs.addFlashAttribute("msg", "Errore nel caricamento del file");
				redirectAttrs.addFlashAttribute("msgClass", "error");
				if (pathUrl.isEmpty()) {
					return "redirect:home";
				}
				return "redirect:home/" + pathUrl;
			}
		}
		String msg = new String();
		redirectAttrs.addFlashAttribute("utente", utente);
		redirectAttrs.addFlashAttribute("msgBool", true);
		if (fileNames.size() == 1) {
			msg = "Il file <ul><li>" + fileNames.get(0) + "</li></ul> è stato caricato con successo";
		} else {
			msg = "I file <ul>";
			for (int i=0; i<fileNames.size(); i++) {
				msg += "<li>" + fileNames.get(i) + "</li>";
			}
			msg += "</ul> sono stati caricati con successo";
		}
		redirectAttrs.addFlashAttribute("msg", msg);
		redirectAttrs.addFlashAttribute("msgClass", "success");
		if (pathUrl.isEmpty()) {
			return "redirect:home";
		}
		return "redirect:home/" + pathUrl;
	}
}
