package it.polito.ai.polibox.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import it.polito.ai.polibox.entity.Utente;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.crsh.shell.impl.command.system.repl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DownloadController {
	@Autowired
	ServletContext servletContext;
	
	@RequestMapping(value = "/home/{file_name}", method = RequestMethod.GET)
	public String getFile(@PathVariable("file_name") String fileName, Model model, HttpSession session, HttpServletResponse response) {
		Utente utente = (Utente) session.getAttribute("utente");
		if (utente == null || utente.getEmail() == null) {
			return "index";
		}
		
		String path = utente.getHome_dir() + "\\" + fileName;
		File file = new File(path);
		try {
			FileInputStream input = new FileInputStream(file);
			
			String mimeType = servletContext.getMimeType(path);
			if (mimeType == null) {
				mimeType = "application/octet-stream";
			}
			response.setContentType(mimeType);
			response.setContentLength((int) file.length());
			
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", file.getName());
			
			response.setHeader(headerKey, headerValue);
			
			try {
				OutputStream output = response.getOutputStream();
				byte[] buffer = new byte[4096];
				int bytesRead = -1;
				while ((bytesRead=input.read(buffer)) != -1) {
					output.write(buffer, 0, bytesRead);
				}
				
				input.close();
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return "home";
	}
	
}
