package it.polito.ai.polibox.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.polito.ai.polibox.dao.CondivisioneDAO;
import it.polito.ai.polibox.dao.SincronizzazioniPendentiDAO;
import it.polito.ai.polibox.entity.Condivisione;
import it.polito.ai.polibox.entity.SincronizzazioniPendenti;
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

@Controller
public class DownloadController {
	@Autowired
	ServletContext servletContext;
	@Autowired
	CondivisioneDAO condivisioneDAO;
	
	@RequestMapping(value = "/home/**", method = RequestMethod.GET)
	public String getFile(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		Utente utente = (Utente) session.getAttribute("utente");
		if (utente == null || utente.getEmail() == null) {
			return "index";
		}
		String pattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);  
		String path = new AntPathMatcher().extractPathWithinPattern(pattern, request.getServletPath());
		String filePath = utente.getHome_dir() + "\\Polibox\\" + path.replace("/", "\\");
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
		System.out.println("Attributo di sessione ownercond: "+session.getAttribute("ownerCond"));
		if (session.getAttribute("ownerCond")== null) {
			for (Condivisione c : condivisioneDAO.getCondivisioniOwner(utente.getId())) {
				if (c.getDirPath().equals(filePath)) {
					session.setAttribute("ownerCond", path);
					session.setAttribute("cId", c.getId());
					System.out.println("Setto attributo di sessione ownerCond a : "+path);
					break;
				}
			}
		} else {
			String p =(String) session.getAttribute("ownerCond");
			int l1 = (p.split("/").length);
			int l2 =  (path.split("/").length);
			System.out.println("Confronto: "+p+" lungo "+l1+ " con: "+path+" lungo:"+l2);
			if ( l1>l2) {
				session.removeAttribute("ownerCond");
				session.removeAttribute("cId");
				System.out.println("Rimuovo attributo di sessione ownerCond a : ");
			} 
				
		}
		List<String> owner_sd_list = new ArrayList<String>();
		HashMap<Long,String> pending_sd_list = new HashMap<Long,String>();
		for (Condivisione c: condivisioneDAO.getCondivisioni(utente.getId())) {
			String dirPath = c.getDirPath().substring(c.getDirPath().lastIndexOf("\\") + 1);
			if (c.getState() == 0)
				pending_sd_list.put(c.getId(), dirPath);
		}
		for (Condivisione c: condivisioneDAO.getCondivisioniOwner(utente.getId())) {
			if (c.getState() == 1)
				owner_sd_list.add(c.getDirPath());
		}
		model.addAttribute("owner_sd_list", owner_sd_list);
		model.addAttribute("pending_sd_list", pending_sd_list);
		
		// aggiorna il path
		model.addAttribute("pathDir", filePath);
		model.addAttribute("pathUrl", path);
		return "home";
	}
	
}
