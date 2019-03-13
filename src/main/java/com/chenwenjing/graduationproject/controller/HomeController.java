package com.chenwenjing.graduationproject.controller;

import com.chenwenjing.graduationproject.constant.GraduationProjectConstant;
import com.chenwenjing.graduationproject.data.User;
import com.chenwenjing.graduationproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller(GraduationProjectConstant.API)
public class HomeController {

    @Autowired
    private UserService userService;

    @RequestMapping("/index")
    public String index(@RequestParam(name = "name") String name,
                        Model model) {
        User user = userService.getByName(name);
        model.addAttribute("user", user);
        return "index";
    }

}
