package pl.maciejpajak;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import pl.maciejpajak.testing.event.event.GameEndEvent;

@SpringBootApplication
public class BettingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BettingApplication.class, args);
	}
	
//	@Autowired
//    private ApplicationEventPublisher applicationEventPublisher;
//	
//	
//    private static final Logger log = LoggerFactory.getLogger(BettingApplication.class);
//
//	@Bean 
//	CommandLineRunner init() {
//	    log.warn("puslihing event in command line runner");
//	    applicationEventPublisher.publishEvent(new GameEndEvent(this, 1L));
//	    log.warn("returnig from commandlinerunner");
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
