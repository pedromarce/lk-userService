/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author pedromarce
 */
@RestController
public class UserRestController {
    
    @Autowired
    private UserService userService;
    
    @RequestMapping(value="/users",method=RequestMethod.POST)
    public UserEntity addUser(@RequestBody UserEntity userEntity) {
        return userService.addUser(userEntity);
    }
    
    @RequestMapping(value="/users/{userId}/connections/{connectId}",method=RequestMethod.PUT)
    public UserEntity addUserConnection(@PathVariable("userId") long userId, @PathVariable("connectId") long connectionId) {
        return userService.addConnection(userId, connectionId);
    }

    @RequestMapping(value="/users/{id}/connections",method=RequestMethod.GET)
    public Set<UserEntity> getConnections(@PathVariable("id") long userId) {
        return (Set<UserEntity>) userService.getConnections(userId);
    }
    
}
