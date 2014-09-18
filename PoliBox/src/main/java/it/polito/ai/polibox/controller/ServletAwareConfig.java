package it.polito.ai.polibox.controller;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

public class ServletAwareConfig extends Configurator{

	@Override
	public void modifyHandshake(ServerEndpointConfig sec,
			HandshakeRequest request, HandshakeResponse response) {
		HttpSession httpSession = (HttpSession) request.getHttpSession();
		sec.getUserProperties().put("httpSession", httpSession);
	}

}
