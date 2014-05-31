package it.polito.ai.polibox.controller;

import it.polito.ai.polibox.entity.Utente;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerMapping;

@Controller
public class PathController {
	@RequestMapping(value = "/dir/**", method = RequestMethod.GET)
	public String getFile(Model model, HttpSession session, HttpServletRequest request) {
		Utente utente = (Utente) session.getAttribute("utente");
		if (utente == null || utente.getEmail() == null) {
			return "index";
		}
		String pattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);  
		String path = new AntPathMatcher().extractPathWithinPattern(pattern, request.getServletPath());
		System.out.println(path);
		String pathDir = utente.getHome_dir() + "\\" + path;
		model.addAttribute("pathDir", pathDir);
		model.addAttribute("pathUrl", path);
		return "home";
	}
}
