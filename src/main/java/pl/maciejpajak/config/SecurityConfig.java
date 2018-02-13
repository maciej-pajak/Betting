//package pl.maciejpajak.config;
//
//import javax.sql.DataSource;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//import pl.maciejpajak.security.UserDetailServiceImpl;
//
//@Configuration
////@EnableWebSecurity
////@EnableGlobalMethodSecurity(prePostEnabled=true)  // TODO
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//    
//    @Autowired
//    DataSource dataSource;
//    
//    @Bean
//    public UserDetailsService customUserDetailsService() { 
//        return new UserDetailServiceImpl();
//    }
//    
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth)
//            throws Exception {
//        auth.
//            userDetailsService(customUserDetailsService()).passwordEncoder(passwordEncoder());
//    }
//    
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        
//        // TODO
//        http.
//            authorizeRequests()
//                .antMatchers("/").permitAll().and().csrf().disable();
////                .antMatchers("/login").permitAll()
////                .antMatchers("/register").permitAll()
////                .antMatchers("/admin/**").hasAuthority("ADMIN")
////                .antMatchers("/**").hasAnyAuthority("USER", "ADMIN")
////                .anyRequest().authenticated()
////                .and().csrf().disable()
////                .formLogin()
//////                    .loginPage("/login")
//////                    .failureUrl("/login?error=true")
////                .defaultSuccessUrl("/", true)
////                .usernameParameter("email")
////                .passwordParameter("password")
////                .and()
////                .logout()
////                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
////                    .logoutSuccessUrl("/")
////                .and().exceptionHandling()
////                .accessDeniedPage("/access-denied");
//    }
//    
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web
//           .ignoring()
//           .antMatchers("/resources/**");
//    }
//    
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//}
