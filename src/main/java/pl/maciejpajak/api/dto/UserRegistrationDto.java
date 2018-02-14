package pl.maciejpajak.api.dto;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import pl.maciejpajak.api.dto.validation.PasswordMatches;

@Getter
@Setter
@PasswordMatches
public class UserRegistrationDto {
    
    @NotBlank
    @Length(min = 5)
    private String login;
    
    @Email
    private String email;
    
    @Length(min = 6)
    private String password;
    private String passwordRepetition;

}
