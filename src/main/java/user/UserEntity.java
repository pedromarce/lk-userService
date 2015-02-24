/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

/**
 *
 * @author pedromarce
 */
@NodeEntity
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @GraphId 
    private Long id;
    private String name;

    public UserEntity() {}
    public UserEntity(String name) { this.name = name; }
    
    @JsonIgnore
    @RelatedTo(type="connection", direction=Direction.BOTH)
    private @Fetch Set<UserEntity> connections;

    public void addConnection(UserEntity userEntity) {
        if (connections == null) {
            connections = new HashSet<UserEntity>();
        }
        connections.add(userEntity);
    }

    public Iterable<UserEntity> getUserConnections() {
        if (connections == null) {
            connections = new HashSet<UserEntity>();
        }        
        return new HashSet<UserEntity>(connections);
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }
    
}
