package it.polito.ai.polibox;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RegistrationController {
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String showRegistrationForm(Model model) {
		model.addAttribute("utente", new Utente());
		return "registration";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registrationSubmit(@ModelAttribute @Valid Utente utente, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			return "registration";
		}
		model.addAttribute("utente", utente);
		return "registrationresult";
	}

}
