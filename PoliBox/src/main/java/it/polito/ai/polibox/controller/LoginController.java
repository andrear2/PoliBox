package it.polito.ai.polibox.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import it.polito.ai.polibox.dao.UtenteDAO;
import it.polito.ai.polibox.entity.Utente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {
	@Autowired
	private UtenteDAO utenteDAO;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String showLoginForm(Model model) {
		model.addAttribute("utente", new Utente());
		return "login";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginSubmit(@ModelAttribute @Valid Utente utente, BindingResult bindingResult, Model model, HttpSession session) {
		if (bindingResult.hasErrors()) {
			return "login";
		}
		
		Utente u = utenteDAO.getUtente(utente.getEmail(), utente.getPassword());
		if (u == null || u.getEmail() == null) {
			model.addAttribute("error", true);
			return "login";
		}
		model.addAttribute("utente", u);
		session.setAttribute("utente", u);
		return "redirect:home";
	}
}
