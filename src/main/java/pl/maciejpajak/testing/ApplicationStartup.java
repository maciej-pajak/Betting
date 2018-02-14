package pl.maciejpajak.testing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import pl.maciejpajak.domain.bet.BetOption;
import pl.maciejpajak.domain.game.Game;
import pl.maciejpajak.exception.BaseEntityNotFoundException;
import pl.maciejpajak.repository.BetOptionRepository;
import pl.maciejpajak.repository.GameRepository;
import pl.maciejpajak.repository.RoleRepository;
import pl.maciejpajak.repository.UserRepository;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
    
    
    private static final Logger log = LoggerFactory.getLogger(ApplicationStartup.class);


    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private RoleRepository roleRepo;
    
    @Autowired
    private GameRepository gameRepository;
   
    
    @Autowired
    private BetOptionRepository betOptionRepository;
    
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    
    @Override
    public void onApplicationEvent(ApplicationReadyEvent arg0) {
//        System.out.println("App has started!");
//        Role r = new Role();
//        r.setName("ROLE_USER");
//        r = roleRepo.save(r);
//        Set<Role> roles = new HashSet<>();
//        roles.add(r);
//        
//        BCryptPasswordEncoder e = new BCryptPasswordEncoder();
//        User u = new User();
//        u.setLogin("user");
//        u.setPassword(e.encode("user"));
//        u.setRoles(roles);
//        userRepo.save(u);
        
//        subRepo.findOne(1L);
       
//        try {
//            resolveBetOption(new BetOption());
//        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (ScriptException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        
    }
    
    public boolean resolveBetOption(BetOption betOption) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ScriptException {
        Long start = System.nanoTime();
//        Long start2 = System.nanoTime();
//        ScriptEngineManager mgr = new ScriptEngineManager();
//        ScriptEngine engine = mgr.getEngineByName("JavaScript");
//        log.info("create engine time: " + (System.nanoTime() - start2)/1000000 + " ms");
        Long gameId = 1L; // TODO game just finished
        Game game = gameRepository.findOneByIdAndVisible(gameId, true).orElseThrow(() -> new BaseEntityNotFoundException(gameId));
        List<BetOption> betOptions = betOptionRepository.findAllByBetGameIdAndVisible(gameId, true);
        for (BetOption bo : betOptions) {
//            System.out.println("betoption =============");
            Pattern r = Pattern.compile("\\{([^\\}]+)\\}");
            String winCondition = bo.getWinCondition();
            // Now create matcher object.
            Matcher m = r.matcher(winCondition);
            int i = 0;
            while (m.find( )) {
                i++;
//               System.out.println(i + " Found value: " + m.group(0) );
//               System.out.println(i + " Found value: " + m.group(1) );
               Object o = game;
               String[] names = m.group(1).split("\\.");
               for (int j = 0 ; j < names.length - 1 ; j++) {
//                   System.out.println("names[" + j + "] = " + names[j]);
                   String name = names[j+1];
                   Method method = Arrays.asList(o.getClass().getMethods()).stream()
                           .filter(meth -> meth.getName().equalsIgnoreCase("get" + name)).findFirst().orElseThrow(() -> new RuntimeException());
                   o = method.invoke(o, null);
//                   System.out.println(o.toString());
               }
               winCondition = winCondition.replace(m.group(0), o.toString());
//               System.out.println("win condition: " + winCondition);
               
            }
            
            Long stop = (System.nanoTime() - start) / 1000000;
            log.info("win condition: " + winCondition);
//            log.info("win result", engine.eval(winCondition));
            log.info("execution time " + stop + " ms");
        }

        return false;
    }
    

}
