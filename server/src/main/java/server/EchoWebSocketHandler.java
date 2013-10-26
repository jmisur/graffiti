package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.adapter.TextWebSocketHandlerAdapter;

public class EchoWebSocketHandler extends TextWebSocketHandlerAdapter {

	private static Logger log = LoggerFactory.getLogger(EchoWebSocketHandler.class);

	@Override
	public void afterConnectionEstablished(WebSocketSession session) {
		log.debug("Opened new session in instance " + this);
	}

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();
		log.info("Received " + payload);
		String echoMessage = "YEAAAHH " + payload;
		log.debug("Sending " + echoMessage);
		session.sendMessage(new TextMessage(echoMessage));
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		session.close(CloseStatus.SERVER_ERROR);
	}

}