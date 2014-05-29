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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	public String cambiaNomeSubmit(@RequestParam(value="nome") String nome, @RequestParam(value="cognome") String cognome, RedirectAttributes redirectAttrs, HttpSession session) {
		Utente utente = (Utente) session.getAttribute("utente");
		if (utente == null || utente.getEmail() == null) {
			return "index";
		}
		utente.setNome(nome);
		utente.setCognome(cognome);
		session.setAttribute("utente", utente);
		utenteDAO.updateUtente(utente);
		redirectAttrs.addFlashAttribute("utente", utente);
		redirectAttrs.addFlashAttribute("msgBool", true);
		redirectAttrs.addFlashAttribute("msg", "Nome aggiornato con successo");
		redirectAttrs.addFlashAttribute("msgClass", "success");
		return "redirect:account";
	}
	
	@RequestMapping(value = "/cambiaEmail", method = RequestMethod.POST)
	public String cambiaEmailSubmit(@RequestParam(value="email") String email, @RequestParam(value="confermaEmail") String confermaEmail, @RequestParam(value="password") String password, RedirectAttributes redirectAttrs, HttpSession session) {
		Utente utente = (Utente) session.getAttribute("utente");
		if (utente == null || utente.getEmail() == null) {
			return "index";
		}
		for (Utente u: utenteDAO.getUtenti()) {
			if (u.getEmail().equals(email)) {
				redirectAttrs.addFlashAttribute("msgBool", true);
				redirectAttrs.addFlashAttribute("msg", "L'email inserita è già associata ad un account PoliBox");
				redirectAttrs.addFlashAttribute("msgClass", "error");
				return "redirect:account";
			}
		}
		if (email.equals(confermaEmail) && password.equals(utente.getPassword())) {
			utente.setEmail(email);
			session.setAttribute("utente", utente);
			utenteDAO.updateUtente(utente);
			redirectAttrs.addFlashAttribute("msgBool", true);
			redirectAttrs.addFlashAttribute("msg", "Email aggiornata con successo");
			redirectAttrs.addFlashAttribute("msgClass", "success");
		}
		redirectAttrs.addFlashAttribute("utente", utente);
		return "redirect:account";
	}
	
	@RequestMapping(value = "/cambiaPassword", method = RequestMethod.POST)
	public String cambiaPasswordSubmit(@RequestParam(value="vecchiaPassword") String vecchiaPassword, @RequestParam(value="nuovaPassword") String nuovaPassword, RedirectAttributes redirectAttrs, HttpSession session) {
		Utente utente = (Utente) session.getAttribute("utente");
		if (utente == null || utente.getEmail() == null) {
			return "index";
		}
		if (vecchiaPassword.equals(utente.getPassword())) {
			utente.setPassword(nuovaPassword);
			session.setAttribute("utente", utente);
			utenteDAO.updateUtente(utente);
			redirectAttrs.addFlashAttribute("msgBool", true);
			redirectAttrs.addFlashAttribute("msg", "Password aggiornata con successo");
			redirectAttrs.addFlashAttribute("msgClass", "success");
		}
		redirectAttrs.addFlashAttribute("utente", utente);
		return "redirect:account";
	}
}
