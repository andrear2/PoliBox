package it.polito.ai.polibox.controller;

import java.io.BufferedReader;
import java.io.IOException;

import it.polito.ai.polibox.dao.DispositivoDAO;
import it.polito.ai.polibox.dao.UtenteDAO;
import it.polito.ai.polibox.entity.Dispositivo;
import it.polito.ai.polibox.entity.Utente;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ClientAuthenticationController extends HttpServlet {
	@Autowired
	private UtenteDAO utenteDAO;
	@Autowired
	private DispositivoDAO dispositivoDAO;
	
	@RequestMapping(value = "/clientAuthentication", method = RequestMethod.GET)
	public void doPost(HttpServletRequest request, HttpServletResponse response, @RequestParam("user") String user, @RequestParam("password") String password,@RequestParam("dispositivo") String dispositivo) { 
		BufferedReader br;
		try {
//			String user = request.getParameter("utente");
//			String password = request.getParameter("password");
			System.out.println(user+password);
			Utente u = utenteDAO.getUtente(user, password);
			if (u == null ) {
				response.getWriter().write("FAILED");
			} else{
				Dispositivo d;
				if(dispositivo.equals("-1")){
					d = new Dispositivo(u.getId());
					dispositivoDAO.addDispositivo(d);
					response.getWriter().write("OK:"+d.getId());
				}else{
					response.getWriter().write("OK");
				}
				
			}
			
			response.getWriter().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}