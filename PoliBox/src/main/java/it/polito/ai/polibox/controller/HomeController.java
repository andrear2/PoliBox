package it.polito.ai.polibox.controller;

import it.polito.ai.polibox.entity.Utente;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
	public String home() {		
		return "redirect:home";
	}
	
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String showHome(Model model, HttpSession session) {
		Utente u = (Utente) session.getAttribute("utente");
		if (u == null || u.getEmail() == null) {
			return "index";
		}
		model.addAttribute("utente", u);
		return "home";
	}
}
