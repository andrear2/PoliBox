//package it.polito.ai.polibox.controller;
//
//import javax.inject.Inject;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.config.annotation.EnableWebSocket;
//import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
//import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
//
//@Configuration
//@EnableWebSocket
//@ComponentScan("com.my.package")
//public class WebSocketConfig implements WebSocketConfigurer {
//
//  @Value("${ws.url}")
//  String webSocketUrl;
//
//  @Inject
//  WebSocketHandler webSocketHandler;
//
//  @Override
//  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//    registry.addHandler(webSocketHandler, webSocketUrl);
//  }
//}
