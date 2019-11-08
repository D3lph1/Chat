package com.simbirsoft.chat.controller.auth;

import com.simbirsoft.chat.dto.SignupUserDTO;
import com.simbirsoft.chat.exceptions.UserAlreadyExistsException;
import com.simbirsoft.chat.exceptions.UserInitializationException;
import com.simbirsoft.chat.service.auth.SignupUserService;
import com.simbirsoft.chat.service.http.Response;
import com.simbirsoft.chat.util.MessageUtil;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class SignupController {
    private final SignupUserService signupService;

    private final MessageSource messageSource;

    public SignupController(SignupUserService signupService, MessageSource messageSource) {
        this.signupService = signupService;
        this.messageSource = messageSource;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET, name = "auth.signup.render")
    public ModelAndView render() {
        return new ModelAndView("auth/signup");
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST, name = "auth.signup.signup")
    public ResponseEntity<Response> signup(@Valid @RequestBody SignupUserDTO userDTO) throws UserInitializationException {
        try {
            signupService.signup(userDTO);
        } catch (UserAlreadyExistsException e) {
            String message = MessageUtil.retrieveFromSource("auth.signup.already_exists", messageSource);

            return new ResponseEntity<>(
                    new Response("user_already_exists").addError(message), HttpStatus.CONFLICT
            );
        }

        return new ResponseEntity<>(new Response(Response.STATUS_SUCCESS), HttpStatus.OK);
    }
}
