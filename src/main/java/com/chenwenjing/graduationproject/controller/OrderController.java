package com.chenwenjing.graduationproject.controller;

import com.chenwenjing.graduationproject.data.File;
import com.chenwenjing.graduationproject.data.Order;
import com.chenwenjing.graduationproject.data.User;
import com.chenwenjing.graduationproject.service.FileService;
import com.chenwenjing.graduationproject.service.OrderService;
import com.chenwenjing.graduationproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;
    @Autowired
    private HttpSession httpSession;

    @PostMapping("/order")
    public RedirectView create(Order order, Model model) {
        orderService.insert(order);
        User user = (User) httpSession.getAttribute("user");
        fileService.like(user.getId(), order.getFileId(), true);
        RedirectView redirectView = new RedirectView("/file/" + order.getFileId());
        return redirectView;
    }

    @PostMapping("/order/updateState")
    @ResponseBody
    public void allowFile(Order order, Model model) {
        orderService.allow(order.getId());
    }

    @GetMapping("/order/manage")
    public String list(Model model) {
        List<Order> orders = orderService.list();
        model.addAttribute("orders", orders);

        List<Integer> userIds = orders.stream().map(order -> order.getUserId()).collect(Collectors.toList());
        Map<Integer, User> userMap = new HashMap<>();
        for (int userId : userIds) {
            userMap.put(userId, userService.get(userId));
        }

        List<Integer> fileIds = orders.stream().map(order -> order.getFileId()).collect(Collectors.toList());
        Map<Integer, File> fileMap = new HashMap<>();
        for (int fileId : fileIds) {
            fileMap.put(fileId, fileService.get(fileId));
        }

        model.addAttribute("userMap", userMap);
        model.addAttribute("fileMap", fileMap);

        return "order/manage";
    }
}
