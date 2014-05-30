package it.polito.ai.polibox.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import it.polito.ai.polibox.entity.UploadedFiles;
import it.polito.ai.polibox.entity.Utente;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UploadController {
	@RequestMapping(value = "/fileUpload", method = RequestMethod.GET)
	public String showFileUploadForm(Model model, HttpSession session) {
		Utente utente = (Utente) session.getAttribute("utente");
		if (utente == null || utente.getEmail() == null) {
			return "index";
		}
		model.addAttribute("utente", utente);
		model.addAttribute("uploadedFile", new UploadedFiles());
		return "upload";
	}
	
	@RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
	public String fileUploadSubmit(@ModelAttribute("uploadedFile") @Valid UploadedFiles uploadedFiles, BindingResult bindingResult, RedirectAttributes redirectAttrs, HttpSession session) {
		Utente utente = (Utente) session.getAttribute("utente");
		if (utente == null || utente.getEmail() == null) {
			return "index";
		}
		
		List<MultipartFile> fileList = uploadedFiles.getFiles();
		List<String> fileNames = new ArrayList<String>();
		  
		if (fileList == null || fileList.size() == 0 || fileList.get(0).getOriginalFilename() == "") {
			redirectAttrs.addFlashAttribute("utente", utente);
			redirectAttrs.addFlashAttribute("msgBool", true);
			redirectAttrs.addFlashAttribute("msg", "Nessun file selezionato");
			redirectAttrs.addFlashAttribute("msgClass", "error");
			return "redirect:fileUpload";
		}
		
		for (MultipartFile file: fileList) {
			String fileName = file.getOriginalFilename();
			fileNames.add(fileName);
			File dest = new File(utente.getHome_dir() + "\\" + fileName);
			try {
				file.transferTo(dest);
			} catch (IllegalStateException ise) {
				ise.printStackTrace();
				redirectAttrs.addFlashAttribute("utente", utente);
				return "upload";
			} catch (IOException ioe) {
				ioe.printStackTrace();
				redirectAttrs.addFlashAttribute("utente", utente);
				return "upload";
			}
		}
		String msg = new String();
		redirectAttrs.addFlashAttribute("utente", utente);
		redirectAttrs.addFlashAttribute("msgBool", true);
		if (fileNames.size() == 1) {
			msg = "Il file " + fileNames.get(0) + " è stato caricato con successo";
		} else {
			msg = "I file ";
			for (int i=0; i<fileNames.size(); i++) {
				if (fileNames.indexOf(i) == fileNames.size()) {
					msg += "\"" + fileNames.get(i) + "\"";
				} else {
					msg += "\"" + fileNames.get(i) + "\", ";
				}
			}
			msg += " sono stati caricati con successo";
		}
		redirectAttrs.addFlashAttribute("msg", msg);
		redirectAttrs.addFlashAttribute("msgClass", "success");
		return "redirect:home";
	}
}
