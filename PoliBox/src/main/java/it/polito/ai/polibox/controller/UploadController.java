package it.polito.ai.polibox.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import it.polito.ai.polibox.entity.UploadedFile;
import it.polito.ai.polibox.entity.Utente;
import it.polito.ai.polibox.validator.FileValidator;

import org.springframework.beans.factory.annotation.Autowired;
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
	@Autowired FileValidator fileValidator;

	@RequestMapping(value = "/fileUpload", method = RequestMethod.GET)
	public String showFileUploadForm(Model model, HttpSession session) {
		Utente utente = (Utente) session.getAttribute("utente");
		if (utente == null || utente.getEmail() == null) {
			return "index";
		}
		model.addAttribute("utente", utente);
		model.addAttribute("uploadedFile", new UploadedFile());
		return "upload";
	}
	
	@RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
	public String fileUploadSubmit(@ModelAttribute @Valid UploadedFile uploadedFile, BindingResult bindingResult, RedirectAttributes redirectAttrs, HttpSession session) {
		Utente utente = (Utente) session.getAttribute("utente");
		if (utente == null || utente.getEmail() == null) {
			return "index";
		}
		
		fileValidator.validate(uploadedFile, bindingResult);
		  
		if (bindingResult.hasErrors()) {
			redirectAttrs.addFlashAttribute("utente", utente);
			return "upload";
		}
		
		MultipartFile file = uploadedFile.getFile();
		String fileName = file.getOriginalFilename();
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
		redirectAttrs.addFlashAttribute("utente", utente);
		redirectAttrs.addFlashAttribute("msgBool", true);
		redirectAttrs.addFlashAttribute("msg", "Il file \"" + fileName + "\" � stato caricato con successo");
		redirectAttrs.addFlashAttribute("msgClass", "success");
		return "redirect:home";
	}
}
