package com.chenwenjing.graduationproject.controller;

import com.chenwenjing.graduationproject.data.File;
import com.chenwenjing.graduationproject.data.LogData;
import com.chenwenjing.graduationproject.data.Task;
import com.chenwenjing.graduationproject.data.User;
import com.chenwenjing.graduationproject.service.FileService;
import com.chenwenjing.graduationproject.service.OrderService;
import com.chenwenjing.graduationproject.service.TaskService;
import com.chenwenjing.graduationproject.utils.CSVWriter;
import com.chenwenjing.graduationproject.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.chenwenjing.graduationproject.utils.FileUtil.OUTPUT_PATH;

@Controller
public class FileController {

    @Autowired
    private FileService fileService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HttpSession httpSession;

    private static final String[] HEADERS = new String[]{"stemId", "species", "DBH", "stemType", "logNum", "assort", "diamToUb", "length"};

    @GetMapping("/file/upload")
    public String upload(Model model) {
        return "file/upload";
    }

    @PostMapping("/file/upload")
    public String uploadFile(HttpServletRequest request,
                             HttpServletResponse response,
                             @RequestParam("file") MultipartFile[] file) throws Exception {
        MultipartFile f = file[0];
        String originFilename = FileUtil.uploadOriginFile(request, f);

        List<LogData> logData;
        if (f.getOriginalFilename().endsWith("pri")) {
            logData = FileUtil.analysisPri(f);
        } else {
            logData = FileUtil.analysisXml(f);
        }

        List<String[]> csvData = new ArrayList<>();
        for (LogData item : logData) {
            String[] data = new String[8];
            data[0] = String.valueOf(item.getStemId());
            data[1] = String.valueOf(item.getSpecies());
            data[2] = String.valueOf(item.getDbh());
            data[3] = String.valueOf(item.getType());
            data[4] = String.valueOf(item.getLogId());
            data[5] = String.valueOf(item.getAssort());
            data[6] = String.valueOf(item.getDiam());
            data[7] = String.valueOf(item.getLength());
            csvData.add(data);
        }

        String outputPath = OUTPUT_PATH + originFilename.split("\\.", 2)[0] + ".csv";
        CSVWriter.write(outputPath, HEADERS, csvData);

        File toSave = new File();
        toSave.setName(f.getOriginalFilename());
        toSave.setSize((int) f.getSize());
        toSave.setOriginUrl(originFilename);
        toSave.setOutputUrl(originFilename.split("\\.", 2)[0] + ".csv");

        User user = (User) httpSession.getAttribute("user");
        toSave.setReporter(user.getName());

        Task task = new Task();
        task.setSpecies(String.join(",", csvData.stream().map(data -> data[1]).distinct().collect(Collectors.toList())));
        task.setStemCount((int) csvData.stream().map(data -> data[0]).distinct().count());
        task.setLogCount(csvData.size());
        task.setWorkTime(logData.get(0).getWorkTime());
        task.setLogAssort(String.join(",", csvData.stream().map(data -> data[5]).distinct().collect(Collectors.toList())));
        task.setMinDBH(csvData.stream().mapToInt(data -> Integer.valueOf(data[2])).min().getAsInt());
        task.setMaxDBH(csvData.stream().mapToInt(data -> Integer.valueOf(data[2])).max().getAsInt());
        task.setMinLength(csvData.stream().mapToInt(data -> Integer.valueOf(data[7])).min().getAsInt());
        task.setMinLength(csvData.stream().mapToInt(data -> Integer.valueOf(data[7])).max().getAsInt());
        task = taskService.create(task);
        toSave.setTaskId(task.getId());

        fileService.insert(toSave);

        return "redirect:/file/available";
    }

    @GetMapping("file/search")
    public String search(@RequestParam("name") String name,
                         Model model) {
        List<File> files = fileService.search(name);
        model.addAttribute("files", files);
        return "file/available";
    }

    @GetMapping("/file/manage")
    public String fileManage(Model model) {
        List<File> files = fileService.list(-1);
        model.addAttribute("files", files);
        return "file/manage";
    }

    @GetMapping("/file/{fileId:[\\d]+}")
    public String getFile(@PathVariable("fileId") int fileId,
                          Model model) {
        File file = fileService.get(fileId);
        model.addAttribute("file", file);
        Task task = taskService.get(file.getTaskId());
        model.addAttribute("task", task);

        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("allow", false);
            model.addAttribute("submitted", false);
            model.addAttribute("like", false);
        } else {
            model.addAttribute("allow", orderService.checkOrder(user.getId(), fileId));
            model.addAttribute("submitted", orderService.checkSubmit(user.getId(), fileId));
            model.addAttribute("like", fileService.checkLike(user.getId(), fileId));
        }
        return "file/detail";
    }

    @PostMapping("/file/updateState")
    @ResponseBody
    public void allowFile(File file, Model model) {
        fileService.update(file);
    }

    @GetMapping("/file/available")
    public String listAvailable(Model model) {
        List<File> files = fileService.list(-1);
        model.addAttribute("files", files);
        User user = (User) httpSession.getAttribute("user");
        if (user != null) {
            List<File> likeFiles = fileService.listLike(user.getId());
            List<Integer> likeFileIds = likeFiles.stream().map(file -> file.getId()).collect(Collectors.toList());
            model.addAttribute("likeFileIds", likeFileIds);
        }
        return "file/available";
    }

    @GetMapping("/file/like")
    public String userLike(HttpServletRequest request, Model model) {
        User user = (User) httpSession.getAttribute("user");
        if (user != null) {
            List<File> files = fileService.listLike(user.getId());
            model.addAttribute("files", files);
            return "file/like";
        } else {
            model.addAttribute("files", Collections.emptyList());
            return "file/like";
        }
    }

    @PostMapping("/users/{userId:[\\d]+}/file/{fileId:[\\d]+}/like")
    public String likeFile(HttpServletRequest request,
                           HttpServletResponse response,
                           @PathVariable("userId") int userId,
                           @PathVariable("fileId") int fileId) {
        fileService.like(userId, fileId, true);
        return "redirect:/file/like";
    }

    @PostMapping("/users/{userId:[\\d]+}/file/{fileId:[\\d]+}/dislike")
    public String dislikeFile(HttpServletRequest request,
                              HttpServletResponse response,
                              @PathVariable("userId") int userId,
                              @PathVariable("fileId") int fileId) {
        fileService.like(userId, fileId, false);
        return "redirect:/file/like";
    }

}
