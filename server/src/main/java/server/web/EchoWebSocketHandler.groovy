package server.web

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.adapter.TextWebSocketHandlerAdapter

import server.LiveService

class EchoWebSocketHandler extends TextWebSocketHandlerAdapter {

	private  static Logger log = LoggerFactory.getLogger(EchoWebSocketHandler.class)

	@Autowired
	LiveService live

	@Override
	void afterConnectionEstablished(WebSocketSession session) {
		log.debug("Opened new session in instance " + this)
	}

	@Override
	void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		live.stream(session, message)
	}

	@Override
	void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		session.close(CloseStatus.SERVER_ERROR)
	}
}