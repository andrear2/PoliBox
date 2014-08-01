package it.polito.ai.polibox.controller;

import java.io.BufferedReader;
import java.io.IOException;

import it.polito.ai.polibox.dao.UtenteDAO;
import it.polito.ai.polibox.entity.Utente;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

@WebServlet("/clientAuthentication")
public class ClientAuthenticationController extends HttpServlet {
	@Autowired
	private UtenteDAO utenteDAO;
	
	@Override 
	public void doPost(HttpServletRequest request, HttpServletResponse response) { 
		BufferedReader br;
		try {
			String user = request.getParameter("utente");
			String password = request.getParameter("password");
			System.out.println(user+password);
			Utente u = utenteDAO.getUtente(user, password);
			if (u == null ) {
				response.getWriter().write("FAILED");
			} else
				response.getWriter().write("OK");
			
			response.getWriter().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}