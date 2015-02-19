/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import user.UserEntity;
import user.UserRepository;

/**
 *
 * @author pedromarce
 */

public class UserService {
    
    @Autowired
    UserRepository userRepository;
    
    /**
     *
     * @param userEntity
     * @return
     */
    @Transactional
    public UserEntity addUser (UserEntity userEntity) {
        System.out.println(userEntity.getName());
        userRepository.save(userEntity);
        return userEntity;
    }
    
    @Transactional
    public UserEntity addConnection (long userId, long connectionId) {
        UserEntity user = (UserEntity) userRepository.findOne(userId);
        user.addConnection((UserEntity) userRepository.findOne(connectionId));
        userRepository.save(user);
        return user;
    }
    
    public Iterable<UserEntity> getConnections (long userId) {
        return userRepository.findOne(userId).getConnections();
    }
    
}
