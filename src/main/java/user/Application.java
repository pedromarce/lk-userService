package user;

import java.io.File;
import javax.jms.ConnectionFactory;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.impl.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.core.GraphDatabase;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableJms
@EnableNeo4jRepositories(basePackages = "user")
public class Application  extends Neo4jConfiguration implements CommandLineRunner{

    Application() {
        setBasePackage("user");
    }

    @Bean
    GraphDatabaseService graphDatabaseService() {
        return new GraphDatabaseFactory().newEmbeddedDatabase("lk-userservice.db");
    }
    
    @Bean
    public UserService userService(){
        return new UserService();
    } 
    
    @Bean
    public UserJmsReceiver userJmsReceiver(){
        return new UserJmsReceiver();
    } 

    @Bean
    MessageListenerAdapter getConnectionsAdapter(UserJmsReceiver receiver) {
        MessageListenerAdapter messageListener
                = new MessageListenerAdapter(receiver);
        messageListener.setDefaultListenerMethod("getConnections");
        return messageListener;
    }    
    
    @Bean
    SimpleMessageListenerContainer container(MessageListenerAdapter messageListener,
                                             ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setMessageListener(messageListener);
        container.setConnectionFactory(connectionFactory);
        container.setDestinationName("getConnections");
        return container;
    }
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    GraphDatabase graphDatabase;
    
    public static void main(String[] args) throws Exception {
        FileUtils.deleteRecursively(new File("lk-userservice.db"));
        
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
    
    }

}
