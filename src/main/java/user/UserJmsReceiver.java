/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

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
    public void addUser(String message) throws IOException {
        UserEntity userEntity = mapper.readValue(message,UserEntity.class);
        userService.addUser(userEntity);
    }
    
    @JmsListener(destination = "addUserConnection")
    public void addUserConnection(String message) {
        long userId = 0, connectionId = 0;
        userService.addConnection(userId, connectionId);
    }

    @JmsListener(destination = "getUserConnections")
    public void getUserConnections(final String userId) {
        MessageCreator messageCreator;
        messageCreator = new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                try {
                    return session.createTextMessage(mapper.writer().writeValueAsString(userService.getUserConnections(Long.parseLong(userId))));
                } catch (JsonProcessingException ex) {
                }
                return null;
            }
        };
        jmsTemplate.send("ListUserConnections",messageCreator);
    }
    
}
