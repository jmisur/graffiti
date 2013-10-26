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
		for (int i = 0; i < 100; i++) {
			String path = i + "," + i;
			session.sendMessage(new TextMessage(path));
			Thread.sleep(100);
		}
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		session.close(CloseStatus.SERVER_ERROR);
	}

}