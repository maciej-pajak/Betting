package pl.maciejpajak.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pl.maciejpajak.dto.UserRegistrationDto;
import pl.maciejpajak.service.RegistrationService;

@RestController
public class RegistrationApi {
    
    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/register")
    public void registerUser(@RequestBody @Valid UserRegistrationDto userDto) {
        registrationService.registerUser(userDto);
    }
    
}
