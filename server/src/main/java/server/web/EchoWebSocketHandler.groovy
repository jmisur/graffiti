package server.web

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.adapter.TextWebSocketHandlerAdapter

class EchoWebSocketHandler extends TextWebSocketHandlerAdapter {

	private  static Logger log = LoggerFactory.getLogger(EchoWebSocketHandler.class)

	@Override
	void afterConnectionEstablished(WebSocketSession session) {
		log.debug("Opened new session in instance " + this)
	}

	@Override
	void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		def polylines  = [
			"30.5,31 34.5,151",
			"16.5,88 98.5,79",
			"74.5,40 90.5,132",
			"97.5,161 131.5,40",
			"109.5,31 205.5,147",
			"99.5,101 206.5,101",
			"287.5,43 247.5,68",
			"234.5,65 242.5,107",
			"243.63547,109.068103 256.36453,150.931898",
			"263.5,159 301.5,124",
			"342.5,37 355.5,170",
			"393.5,46 356.5,112",
			"433.5,163 354.5,120",
			"89.5,325 99.5,198",
			"164.5,318 85.5,190",
			"62.5,275 167.5,260",
			"202.5,207 229.5,306",
			"160.5,208 255.5,192",
			"305.5,192 248.5,254",
			"305.5,303 246.5,243",
			"361.5,239 297.5,304",
			"294.5,193 358.5,241",
			"393.5,302 385.5,203",
			"367.5,191 449.5,297",
			"444.5,176 446.5,303"
		]
		polylines.each {
			String[] points = it.split(" ")
			points.each {
				String[] xy = it.split(",")
				session.sendMessage(new TextMessage("${xy[0]} ${xy[1]}"))
				Thread.sleep(100)
			}
			session.sendMessage(new TextMessage("stop"))
		}
	}

	@Override
	void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		session.close(CloseStatus.SERVER_ERROR)
	}
}