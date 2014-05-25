package it.polito.ai.polibox.controller;

import javax.servlet.http.HttpSession;

import it.polito.ai.polibox.dao.UtenteDAO;
import it.polito.ai.polibox.entity.Utente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
	
	@RequestMapping(value = "/cambiaNome", method = RequestMethod.POST)
	public String cambiaNomeSubmit(@RequestParam(value="nome") String nome, @RequestParam(value="cognome") String cognome, Model model, HttpSession session) {
		System.out.println(nome+cognome);
		Utente utente = (Utente) session.getAttribute("utente");
		utente.setNome(nome);
		utente.setCognome(cognome);
		session.setAttribute("utente", utente);
		utenteDAO.updateUtente(utente);
		model.addAttribute("utente", utente);
		return "account";
	}
}
