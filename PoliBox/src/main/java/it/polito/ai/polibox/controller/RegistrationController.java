package it.polito.ai.polibox.controller;

import java.io.File;

import it.polito.ai.polibox.dao.UtenteDAO;
import it.polito.ai.polibox.entity.Utente;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RegistrationController {
	@Autowired
	private UtenteDAO utenteDAO;
	
	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public String showRegistrationForm(Model model) {
		model.addAttribute("utente", new Utente());
		return "registration";
	}
	
	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public String registrationSubmit(@ModelAttribute @Valid Utente utente, BindingResult bindingResult, Model model, HttpSession session) {
		if (bindingResult.hasErrors()) {
			return "registration";
		}
		for (Utente u: utenteDAO.getUtenti()) {
			if (u.getEmail().equals(utente.getEmail())) {
				model.addAttribute("error", true);
				model.addAttribute("errorMsg", "L'email inserita è già associata ad un account PoliBox");
				return "registration";
			}
		}
		utenteDAO.addUtente(utente);
		String homePath = "C:\\Polibox uploaded files\\" + utente.getId() + "_" + utente.getCognome() + "_" + utente.getNome();
		File homeDir = new File(homePath);
		homeDir.mkdir();
		utente.setHome_dir(homePath);
		utenteDAO.updateUtente(utente);
		model.addAttribute("utente", utente);
		return "redirect:registrationresult";
	}
}
