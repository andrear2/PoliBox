package it.polito.ai.polibox.controller;

import java.util.HashMap;

import it.polito.ai.polibox.dao.CondivisioneDAO;
import it.polito.ai.polibox.dao.UtenteDAO;
import it.polito.ai.polibox.entity.Condivisione;
import it.polito.ai.polibox.entity.Utente;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CondivisioniController {
	@Autowired
	CondivisioneDAO condivisioneDAO;
	@Autowired
	UtenteDAO utenteDAO;
	
	@RequestMapping(value = "/condivisioni", method = RequestMethod.GET)
	public String showCondivisioni(Model model, HttpSession session) {
		Utente utente = (Utente) session.getAttribute("utente");
		if (utente == null || utente.getEmail() == null) {
			return "index";
		}
		
		HashMap<Condivisione, Utente> owner_sd_list = new HashMap<Condivisione, Utente>();
		HashMap<Condivisione, Utente> pending_owner_sd_list = new HashMap<Condivisione, Utente>();
		HashMap<Condivisione, Utente> sd_list = new HashMap<Condivisione, Utente>();
		HashMap<Condivisione, Utente> pending_sd_list = new HashMap<Condivisione, Utente>();
		for (Condivisione c: condivisioneDAO.getCondivisioni(utente.getId())) {
			//String dirPath = c.getDirPath().substring(c.getDirPath().lastIndexOf("\\") + 1);
			if (c.getState() == 1)
				sd_list.put(c, utenteDAO.getUtenteWithoutTrans(c.getOwnerId()));
			else if (c.getState() == 0)
				pending_sd_list.put(c, utenteDAO.getUtenteWithoutTrans(c.getOwnerId()));
		}
		for (Condivisione c: condivisioneDAO.getCondivisioniOwner(utente.getId())) {
			if (c.getState() == 1)
				owner_sd_list.put(c, utenteDAO.getUtenteWithoutTrans(c.getUserId()));
			else if (c.getState() == 0)
				pending_owner_sd_list.put(c, utenteDAO.getUtenteWithoutTrans(c.getUserId()));
		}
		model.addAttribute("owner_sd_list", owner_sd_list);
		model.addAttribute("pending_owner_sd_list", pending_owner_sd_list);
		model.addAttribute("sd_list", sd_list);
		model.addAttribute("pending_sd_list", pending_sd_list);
		model.addAttribute("utente", utente);
		return "condivisioni";
	}
}
