package com.chenwenjing.graduationproject.controller;

import com.chenwenjing.graduationproject.data.User;
import com.chenwenjing.graduationproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession httpSession;

    @GetMapping("/user/login")
    public String login(Model model) {
        return "login";
    }

    @PostMapping("/user/login")
    public String loginPost(User user, Model model) {
        User existUser = userService.getByNameAndPassword(user.getName(), user.getPassword());
        if (existUser != null) {
            httpSession.setAttribute("user", existUser);
            return "redirect:/file/available";
        } else {
            model.addAttribute("error", "用户名或密码错误，请重新登录！");
            return "login";
        }
    }

    @GetMapping("/user/register")
    public String register(Model model) {
        return "register";
    }

    @PostMapping("/user/register")
    public String registerPost(User newUser, Model model) {
        User oldUser = userService.getByName(newUser.getName());
        if (oldUser != null) {
            model.addAttribute("error", "该账号已存在！");
            return "register";
        } else {
            model.addAttribute("error", "恭喜您，注册成功！");
            userService.register(newUser);
            return "login";
        }
    }

    @GetMapping("/user/manage")
    public String userManage(Model model) {
        List<User> allUsers = userService.list();
        model.addAttribute("userList", allUsers);
        return "user/manage";
    }

    @PostMapping("/user/updateState")
    @ResponseBody
    public void updateState(User user, Model model) {
        userService.updateRole(user.getId(), user.getRole());
    }


}
