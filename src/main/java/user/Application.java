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
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DestinationResolver;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableJms
@EnableNeo4jRepositories(basePackages = "user")
public class Application  extends Neo4jConfiguration implements CommandLineRunner {

    Application() {
        setBasePackage("user");
    }

    @Bean
    public UserService userService(){
        return new UserService();
    } 
    
    @Bean
    public UserJmsReceiver userJmsReceiver(){
        return new UserJmsReceiver();
    } 

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired(required = false)
    private DestinationResolver destinationResolver;
    
    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory =
                new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setDestinationResolver(destinationResolver);
        factory.setPubSubDomain(true);
        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory);
        jmsTemplate.setPubSubDomain(true);
        return jmsTemplate;
    }
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    GraphDatabase graphDatabase;
    
    @Bean
    GraphDatabaseService graphDatabaseService() {
        return new GraphDatabaseFactory().newEmbeddedDatabase("lk-userservice.db");
    }
    
    public static void main(String[] args) throws Exception {
        FileUtils.deleteRecursively(new File("lk-userservice.db"));
        
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
    
    }

}
