package pl.maciejpajak;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class BettingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BettingApplication.class, args);
	}
	
	@Bean // TODO move to configuration file
	public BCryptPasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
//	@Autowired
//    private ApplicationEventPublisher applicationEventPublisher;
//	
//	
//    private static final Logger log = LoggerFactory.getLogger(BettingApplication.class);
//
//	@Bean 
//	CommandLineRunner init() {
//	    BCryptPasswordEncoder en = new BCryptPasswordEncoder();
//	    System.out.println("xxx>" + en.encode("123456"));
//	    return null;
//	}
	
//	@Bean(name = "applicationEventMulticaster")
//    public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
//        SimpleApplicationEventMulticaster eventMulticaster 
//          = new SimpleApplicationEventMulticaster();
//         
//        eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
//        return eventMulticaster;
//    }
	
}
