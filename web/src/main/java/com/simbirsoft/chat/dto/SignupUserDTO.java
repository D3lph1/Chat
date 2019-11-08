package com.simbirsoft.chat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.simbirsoft.chat.validation.constraints.PasswordsEqualConstraint;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@PasswordsEqualConstraint(message = "{auth.signup.password_confirmation}")
public class SignupUserDTO {
    @NotNull(message = "{auth.signup.first_name.not_null}")
    @Length(min = 2, max = 64, message = "{auth.signup.first_name.length}")
    private String firstName;

    @NotNull(message = "{auth.signup.last_name.not_null}")
    @Length(min = 2, max = 64, message = "{auth.signup.last_name.length}")
    private String lastName;

    @NotNull(message = "{auth.signup.email.not_null}")
    @Email(regexp = ".+@.+\\..+", message = "{auth.signup.email.invalid}")
    private String email;

    @NotNull(message = "{auth.signup.password.not_null}")
    @Length(min = 6, max = 64, message = "{auth.signup.password.length}")
    private String password;

    private String passwordConfirmation;

    public SignupUserDTO(
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("email") String email,
            @JsonProperty("password") String password,
            @JsonProperty("passwordConfirmation") String passwordConfirmation) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }
}
