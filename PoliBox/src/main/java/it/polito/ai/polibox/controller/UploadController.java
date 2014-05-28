package it.polito.ai.polibox.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
	public String fileUploadSubmit(@ModelAttribute @Valid UploadedFile uploadedFile, BindingResult bindingResult, Model model, HttpSession session) {
		Utente utente = (Utente) session.getAttribute("utente");
		if (utente == null || utente.getEmail() == null) {
			return "index";
		}
		
		InputStream inputStream = null;
		OutputStream outputStream = null;
		MultipartFile file = uploadedFile.getFile();
		fileValidator.validate(uploadedFile, bindingResult);
		  
		if (bindingResult.hasErrors()) {
			model.addAttribute("utente", utente);
			return "upload";
		}
		
		String fileName = file.getOriginalFilename();
		try {
			inputStream = file.getInputStream();  
		    File newFile = new File("C:\\Users\\Andrea\\Desktop\\file\\" + fileName);  
		    if (!newFile.exists()) {  
		    	newFile.createNewFile();  
		    }  
		    outputStream = new FileOutputStream(newFile);
		    int read = 0;
		    byte[] bytes = new byte[1024];
		    while ((read = inputStream.read(bytes)) != -1) {
		    	outputStream.write(bytes, 0, read);
		    }
		    outputStream.close();
		} catch (IOException e) {
		    e.printStackTrace();
		    model.addAttribute("utente", utente);
			return "upload";
		}
		model.addAttribute("utente", utente);
		model.addAttribute("msgBool", true);
		model.addAttribute("msg", "Il file " + fileName + " è stato caricato con successo");
		model.addAttribute("msgClass", "success");  
		return "home";
	}
}
