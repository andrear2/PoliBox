package it.polito.ai.polibox.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.HashMap;

import it.polito.ai.polibox.dao.CondivisioneDAO;
import it.polito.ai.polibox.dao.UtenteDAO;
import it.polito.ai.polibox.entity.Condivisione;
import it.polito.ai.polibox.entity.Utente;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.HandlerMapping;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	@Autowired
	ServletContext servletContext;
	@Autowired
	CondivisioneDAO condivisioneDAO;
	@Autowired
	UtenteDAO utenteDAO;
	
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
		HashMap<Long,String> pending_sd_list = new HashMap<Long,String>();
		for (Condivisione c: condivisioneDAO.getCondivisioni(u.getId())) {
			String dirPath = c.getDirPath().substring(c.getDirPath().lastIndexOf("\\") + 1);
			if (c.getState() == 1)
				sd_list.put(c.getId(), dirPath);
			else if (c.getState() == 0)
				pending_sd_list.put(c.getId(), dirPath);
		}
		model.addAttribute("sd_list", sd_list);
		model.addAttribute("pending_sd_list", pending_sd_list);
		model.addAttribute("utente", u);
		
		return "home";
	}
	
	@RequestMapping(value = "/Home", method = RequestMethod.GET)
	public String showhome(Model model, HttpSession session) {
		return "redirect:home";
	}
	
	@RequestMapping(value = "/Home/{sharedDir}", method = RequestMethod.POST)
	public String showSharedDirectory(Model model, HttpSession session, @PathVariable String sharedDir, @RequestParam("cId") Long cId, HttpServletRequest request, HttpServletResponse response) {
//		Utente u = (Utente) session.getAttribute("utente");
//		if (u == null || u.getEmail() == null) {
//			return "index";
//		}
//
//		Condivisione condivisione = condivisioneDAO.getCondivisioneWithoutTrans((Long) session.getAttribute("cId"));
//		Utente owner = utenteDAO.getUtenteWithoutTrans(condivisione.getOwnerId());
//		session.setAttribute("cId", cId);
//		
//		String filePath = owner.getHome_dir() + "\\Polibox\\" + sharedDir;
//		
//		HashMap<Long,String> pending_sd_list = new HashMap<Long,String>();
//		for (Condivisione c: condivisioneDAO.getCondivisioni(u.getId())) {
//			String dirPath = c.getDirPath().substring(c.getDirPath().lastIndexOf("\\") + 1);
//			if (c.getState() == 0)
//				pending_sd_list.put(c.getId(), dirPath);
//		}
//		model.addAttribute("pending_sd_list", pending_sd_list);
//		
//		// aggiorna il path
//		model.addAttribute("pathDir", filePath);
//		model.addAttribute("pathUrl", sharedDir);
//		
//		return "homeCond";
		session.setAttribute("cId", cId);
		return "redirect:" + sharedDir;
	}
	
	@RequestMapping(value = "/Home/**", method = RequestMethod.GET)
	public String getFile(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		Utente utente = (Utente) session.getAttribute("utente");
		if (utente == null || utente.getEmail() == null) {
			return "index";
		}
		
		Condivisione condivisione = condivisioneDAO.getCondivisioneWithoutTrans((Long) session.getAttribute("cId"));
		Utente owner = utenteDAO.getUtenteWithoutTrans(condivisione.getOwnerId());
		session.setAttribute("condivisione", condivisione);
		
		String pattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);  
		String path = new AntPathMatcher().extractPathWithinPattern(pattern, request.getServletPath());
		String filePath = owner.getHome_dir() + "\\Polibox\\" + path.replace("/", "\\");
		File file = new File(filePath);
		
		if (file.isFile()) {
			// é stato selezionato un file, esegui il download
			try {
				FileInputStream input = new FileInputStream(file);
				
				String mimeType = servletContext.getMimeType(filePath);
				if (mimeType == null) {
					mimeType = "application/octet-stream";
				}
				response.setContentType(mimeType);
				response.setContentLength((int) file.length());
				
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"", file.getName());
				
				response.setHeader(headerKey, headerValue);
				
				OutputStream output = null;
				try {
					output = response.getOutputStream();
					IOUtils.copy(input, output);
					input.close();
					output.close();
					response.flushBuffer();
				} catch (Exception e) {
					String clientAbortException = "";
					int index = e.toString().indexOf(":");
					if (index != 0) {
						clientAbortException = e.toString().substring(0, index);
					}
					if ("ClientAbortException".equalsIgnoreCase(clientAbortException)) {
						// check it the exception is the ClientAbortException
						// downloading canceled by the client
						if (output != null) {
							try {
								if (output != null) {
									output.close(); // close the output stream
								}
								if (input != null) {
									input.close(); // close the input stream
								}
							} catch (Exception ee){
								// catch nothing
							}
						}
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		HashMap<Long,String> pending_sd_list = new HashMap<Long,String>();
		for (Condivisione c: condivisioneDAO.getCondivisioni(utente.getId())) {
			String dirPath = c.getDirPath().substring(c.getDirPath().lastIndexOf("\\") + 1);
			if (c.getState() == 0)
				pending_sd_list.put(c.getId(), dirPath);
		}
		model.addAttribute("pending_sd_list", pending_sd_list);
		
		// aggiorna il path
		model.addAttribute("pathDir", filePath);
		model.addAttribute("pathUrl", path);
		return "homeCond";
	}
}
