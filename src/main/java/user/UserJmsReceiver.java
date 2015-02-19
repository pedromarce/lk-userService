/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.messaging.handler.annotation.SendTo;

/**
 *
 * @author pedromarce
 */
public class UserJmsReceiver {
 
    @Autowired
    ObjectMapper mapper;    

    @Autowired
    JmsTemplate jmsTemplate;    

    @Autowired
    private UserService userService;
    
    @JmsListener(destination = "addUser")
    public UserEntity addUser(String message) throws IOException {
        UserEntity userEntity = mapper.readValue(message,UserEntity.class);
        return userService.addUser(userEntity);
    }
    
    @JmsListener(destination = "addUserConnection")
    public UserEntity addUserConnection(String message) {
        long userId = 0, connectionId = 0;
        return userService.addConnection(userId, connectionId);
    }

    @JmsListener(destination = "getConnections")
    @SendTo("queueOut")
    public void getConnections(final String userId) {
        System.out.println(userId);
        MessageCreator messageCreator;
        messageCreator = new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                try {
                    return session.createTextMessage(mapper.writer().writeValueAsString(userService.getConnections(Long.parseLong(userId))));
                } catch (JsonProcessingException ex) {
                    Logger.getLogger(UserJmsReceiver.class.getName()).log(Level.SEVERE, null, ex);
                }
                return null;
            }
        };
        jmsTemplate.send("ListUserConnections",messageCreator);
    }
    
}
