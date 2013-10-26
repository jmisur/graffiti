package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.config.EnableWebSocket;

import server.web.EchoWebSocketHandler;

@ComponentScan
@EnableAutoConfiguration
@EnableWebSocket
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean(name = "/echo")
	public WebSocketHandler echoWebSocketHandler() {
		return new EchoWebSocketHandler();
	}
}
