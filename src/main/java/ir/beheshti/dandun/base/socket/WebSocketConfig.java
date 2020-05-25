package ir.beheshti.dandun.base.socket;

import ir.beheshti.dandun.base.user.service.GeneralService;
import ir.beheshti.dandun.base.user.service.UtilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private ChatService chatService;
    @Autowired
    private GeneralService generalService;
    @Autowired
    private UtilityService utilityService;

    @Bean
    public ServletServerContainerFactoryBean createWebsocketContainer() {
        ServletServerContainerFactoryBean containerFactoryBean = new ServletServerContainerFactoryBean();
        containerFactoryBean.setMaxBinaryMessageBufferSize(1024000 * 10);
        containerFactoryBean.setMaxTextMessageBufferSize(1024000 * 20);
        return containerFactoryBean;
    }

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new SocketHandler(chatService, generalService, utilityService), "/ws").setAllowedOrigins("*");
    }
}
