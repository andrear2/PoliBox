package it.polito.ai.polibox.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.polito.ai.polibox.dao.CondivisioneDAO;
import it.polito.ai.polibox.entity.Condivisione;
import it.polito.ai.polibox.entity.Utente;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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
	@Autowired
	CondivisioneDAO condivisioneDAO;
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
		HashMap<Long,String> sd_list = new HashMap<Long,String>();
		for (Condivisione c: condivisioneDAO.getCondivisioni(u.getId())) {
			String dirPath = c.getDirPath().substring(c.getDirPath().lastIndexOf("\\") + 1);
			if(c.getState()==1)
				sd_list.put(c.getId(),dirPath);
		}
		model.addAttribute("sd_list",sd_list);
		model.addAttribute("utente", u);
		return "home";
	}
}
