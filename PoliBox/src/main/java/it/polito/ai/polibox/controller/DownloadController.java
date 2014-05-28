package it.polito.ai.polibox.controller;

import it.polito.ai.polibox.entity.Utente;

import java.io.File;

import javax.servlet.http.HttpSession;
import javax.swing.JFileChooser;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DownloadController {
	@RequestMapping(value = "/{file_name}", method = RequestMethod.GET)
	public String getFile(@PathVariable("file_name") String fileName, Model model, HttpSession session) {
		Utente utente = (Utente) session.getAttribute("utente");
		if (utente == null || utente.getEmail() == null) {
			return "index";
		}
		
		JFileChooser jFileChooser = new JFileChooser();
		jFileChooser.setSelectedFile(new File(utente.getHome_dir() + "\\" + fileName));
		jFileChooser.showSaveDialog(null);
		
		model.addAttribute("utente", utente);
		return "home";
	}
}
