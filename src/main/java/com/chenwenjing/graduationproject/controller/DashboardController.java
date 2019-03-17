package com.chenwenjing.graduationproject.controller;

import com.chenwenjing.graduationproject.data.File;
import com.chenwenjing.graduationproject.data.User;
import com.chenwenjing.graduationproject.service.FileService;
import com.chenwenjing.graduationproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;

    @GetMapping("/user/dashboard")
    public String dashboard(Model model) {
        return "dashboard";
    }

    @GetMapping("/user/userManage")
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

    @GetMapping("/user/dataManage")
    public String fileManage(Model model) {
        List<File> files = fileService.list(-1);
        model.addAttribute("fileList", files);
        return "file/manage";
    }

}
