package com.simbirsoft.chat.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {
    @RequestMapping(value = "/login", method = RequestMethod.GET, name = "auth.login.render")
    public ModelAndView render() {
        return new ModelAndView("auth/login");
    }
}
