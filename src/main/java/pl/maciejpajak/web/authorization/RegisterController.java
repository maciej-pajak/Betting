//package pl.maciejpajak.web.authorization;
//
//import javax.validation.Valid;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//
//import pl.maciejpajak.dto.UserDto;
//import pl.maciejpajak.service.UserService;
//
//@Controller
//public class RegisterController {
//
//    @Autowired
//    private AuthorizationService service;
//
//    @GetMapping("/register")
//    public String showRegisterForm(Model model) {
//        model.addAttribute("user", new UserDto());
//        return "register";
//    }
//    
//    @PostMapping("/register")
//    public String processRegistrationRequest(@Valid @ModelAttribute("user") UserDto user, BindingResult result) {
//        if (!result.hasErrors()) {
//            if (!service.exists(user)) {
//                service.registerUser(user);
//                return "redirect:/login";
//            }
//            result.rejectValue("email", "registration.email", "this email is already taken");   // TODO move to file
//        }
//        user.setPassword("");
//        user.setPasswordRepetition("");
//        return "register";
//    }
//    
//}
