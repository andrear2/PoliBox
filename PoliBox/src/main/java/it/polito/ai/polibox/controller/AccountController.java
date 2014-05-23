package it.polito.ai.polibox.controller;

import javax.servlet.http.HttpSession;

import it.polito.ai.polibox.dao.UtenteDAO;
import it.polito.ai.polibox.entity.Utente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AccountController {
	@Autowired
	private UtenteDAO utenteDAO;
	
	@RequestMapping(value = "/account", method = RequestMethod.GET)
	public String showEditAccountForm(Model model, HttpSession session) {
		Utente utente = (Utente) session.getAttribute("utente");
		model.addAttribute("utente", utente);
		return "account";
	}
}
