/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author pedromarce
 */
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    
    
    
}
