package com.simbirsoft.chat.validation.validators;

import com.simbirsoft.chat.dto.SignupUserDTO;
import com.simbirsoft.chat.validation.constraints.PasswordsEqualConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordsEqualConstraintValidator implements ConstraintValidator<PasswordsEqualConstraint, SignupUserDTO> {
    @Override
    public boolean isValid(SignupUserDTO signupUserDTO, ConstraintValidatorContext constraintValidatorContext) {
        return signupUserDTO.getPassword().equals(signupUserDTO.getPasswordConfirmation());
    }
}
