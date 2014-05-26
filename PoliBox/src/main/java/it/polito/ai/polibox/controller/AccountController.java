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
		if (utente == null || utente.getEmail() == null) {
			return "index";
		}
		model.addAttribute("utente", utente);
		return "account";
	}
	
	@RequestMapping(value = "/cambiaNome", method = RequestMethod.POST)
	public String cambiaNomeSubmit(@RequestParam(value="nome") String nome, @RequestParam(value="cognome") String cognome, Model model, HttpSession session) {
		Utente utente = (Utente) session.getAttribute("utente");
		if (utente == null || utente.getEmail() == null) {
			return "index";
		}
		utente.setNome(nome);
		utente.setCognome(cognome);
		session.setAttribute("utente", utente);
		utenteDAO.updateUtente(utente);
		model.addAttribute("utente", utente);
		model.addAttribute("msgBool", true);
		model.addAttribute("msg", "Nome aggiornato con successo");
		model.addAttribute("msgClass", "success");
		return "account";
	}
	
	@RequestMapping(value = "/cambiaNome", method = RequestMethod.GET)
	public String cambiaNomeGet(Model model, HttpSession session) {
		Utente utente = (Utente) session.getAttribute("utente");
		if (utente == null || utente.getEmail() == null) {
			return "index";
		}
		model.addAttribute("utente", utente);
		return "account";
	}
	
	@RequestMapping(value = "/cambiaEmail", method = RequestMethod.POST)
	public String cambiaEmailSubmit(@RequestParam(value="email") String email, @RequestParam(value="confermaEmail") String confermaEmail, @RequestParam(value="password") String password, Model model, HttpSession session) {
		Utente utente = (Utente) session.getAttribute("utente");
		if (utente == null || utente.getEmail() == null) {
			return "index";
		}
		for (Utente u: utenteDAO.getUtenti()) {
			if (u.getEmail().equals(email)) {
				model.addAttribute("msgBool", true);
				model.addAttribute("msg", "L'email inserita è già associata ad un account PoliBox");
				model.addAttribute("msgClass", "error");
				return "account";
			}
		}
		if (email.equals(confermaEmail) && password.equals(utente.getPassword())) {
			utente.setEmail(email);
			session.setAttribute("utente", utente);
			utenteDAO.updateUtente(utente);
			model.addAttribute("msgBool", true);
			model.addAttribute("msg", "Email aggiornata con successo");
			model.addAttribute("msgClass", "success");
		}
		model.addAttribute("utente", utente);
		return "account";
	}
	
	@RequestMapping(value = "/cambiaEmail", method = RequestMethod.GET)
	public String cambiaEmailGet(Model model, HttpSession session) {
		Utente utente = (Utente) session.getAttribute("utente");
		if (utente == null || utente.getEmail() == null) {
			return "index";
		}
		model.addAttribute("utente", utente);
		return "account";
	}
	
	@RequestMapping(value = "/cambiaPassword", method = RequestMethod.POST)
	public String cambiaPasswordSubmit(@RequestParam(value="vecchiaPassword") String vecchiaPassword, @RequestParam(value="nuovaPassword") String nuovaPassword, Model model, HttpSession session) {
		Utente utente = (Utente) session.getAttribute("utente");
		if (utente == null || utente.getEmail() == null) {
			return "index";
		}
		if (vecchiaPassword.equals(utente.getPassword())) {
			utente.setPassword(nuovaPassword);
			session.setAttribute("utente", utente);
			utenteDAO.updateUtente(utente);
			model.addAttribute("msgBool", true);
			model.addAttribute("msg", "Password aggiornata con successo");
			model.addAttribute("msgClass", "success");
		}
		model.addAttribute("utente", utente);
		return "account";
	}
	
	@RequestMapping(value = "/cambiaPassword", method = RequestMethod.GET)
	public String cambiaPasswordGet(Model model, HttpSession session) {
		Utente utente = (Utente) session.getAttribute("utente");
		if (utente == null || utente.getEmail() == null) {
			return "index";
		}
		model.addAttribute("utente", utente);
		return "account";
	}
}
