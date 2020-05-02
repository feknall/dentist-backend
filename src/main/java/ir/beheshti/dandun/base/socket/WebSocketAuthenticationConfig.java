//package ir.beheshti.dandun.base.socket;
//
//import ir.beheshti.dandun.base.security.SecurityConstants;
//import ir.beheshti.dandun.base.user.service.GeneralService;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.simp.config.ChannelRegistration;
//import org.springframework.messaging.simp.stomp.StompCommand;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.messaging.support.ChannelInterceptor;
//import org.springframework.messaging.support.MessageHeaderAccessor;
//import org.springframework.web.socket.config.annotation.EnableWebSocket;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
//import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
//
//import java.util.List;
//
//@Log4j2
//@Configuration
//@EnableWebSocketMessageBroker
//@EnableWebSocket
//public class WebSocketAuthenticationConfig implements WebSocketMessageBrokerConfigurer {
//
//    @Autowired
//    private GeneralService generalService;
//
//    @Bean
//    public ServletServerContainerFactoryBean createWebsocketContainer() {
//        ServletServerContainerFactoryBean containerFactoryBean = new ServletServerContainerFactoryBean();
//        containerFactoryBean.setMaxBinaryMessageBufferSize(1024000 * 10);
//        return containerFactoryBean;
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry
//                .addEndpoint("/ws")
//                .setAllowedOrigins("*");
//    }
//
//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(new ChannelInterceptor() {
//
//            @Override
//            public Message<?> preSend(Message<?> message, MessageChannel channel) {
//                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
//                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//                    List<String> authorization = accessor.getNativeHeader(SecurityConstants.HEADER_STRING);
//                    log.debug(SecurityConstants.HEADER_STRING + "{}", authorization);
//                    String accessToken = authorization.get(0).split(" ")[1];
//                    accessor.setUser(generalService.getAuthentication(accessToken));
//                }
//                return message;
//            }
//
//        });
//
//    }
//
//}
