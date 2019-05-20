package com.chenwenjing.graduationproject.controller;

import com.chenwenjing.graduationproject.data.File;
import com.chenwenjing.graduationproject.service.FileService;
import com.chenwenjing.graduationproject.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DownloadController {

    @Autowired
    private FileService fileService;

    @GetMapping("/file/{fileId:[\\d]+}/downloadOrigin")
    @ResponseBody
    public Object downloadOrigin(@PathVariable("fileId") int fileId){

        File file = fileService.get(fileId);

        ResponseEntity<InputStreamResource> response = null;
        try {
            response = FileUtil.download(file.getOriginUrl(), true);
        } catch (Exception e) {
        }
        return response;
    }

    @GetMapping("/file/{fileId:[\\d]+}/downloadOutput")
    @ResponseBody
    public Object downloadOutput(@PathVariable("fileId") int fileId){

        File file = fileService.get(fileId);

        ResponseEntity<InputStreamResource> response = null;
        try {
            response = FileUtil.download(file.getOutputUrl(), false);
        } catch (Exception e) {
        }
        return response;
    }
}
