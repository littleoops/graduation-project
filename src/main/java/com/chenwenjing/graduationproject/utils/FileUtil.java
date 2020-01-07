package com.chenwenjing.graduationproject.utils;
 
import com.chenwenjing.graduationproject.data.LogData;
import com.chenwenjing.graduationproject.data.Task;
import org.dom4j.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;

public class FileUtil {

    public static final String ORIGIN_PATH = "/Users/wenjing/workspace/graduation-project/src/main/resources/static/files/origin/";
    public static final String OUTPUT_PATH = "/Users/wenjing/workspace/graduation-project/src/main/resources/static/files/output/";

    public static String uploadOriginFile(HttpServletRequest request, MultipartFile file) {
        try {
            file.transferTo(Paths.get(ORIGIN_PATH + file.getOriginalFilename()));
        } catch (IOException e) {
            System.out.println("io error");
        }
        return file.getOriginalFilename();
    }

    public static List<LogData> analysisPri(MultipartFile f) {
        String content = "";
        try {
            content = new String(f.getBytes());
        } catch (Exception e) {
        }

        content = content.replaceAll("\n", "");

        List<String> rows = Arrays.asList(content.split("~"));

        int numberLogCode = 0;
        int numTreeCode = 0;

        int code500 = 0;
        int code501 = 0;
        int code1 = 0;
        int code202 = 0;
        int code301 = 0;

        int code500_1 = 0;
        int code2 = 0;
        int code740 = 0;
        int code741 = 0;

        String workTime = "";
        List<List<Integer>> logList = new ArrayList<>();
        List<List<Integer>> treeList = new ArrayList<>();

        for (String row : rows) {
            String[] rowData = row.split(" ", 3);
            // workTime
            if (StringUtils.isEmpty(workTime) && rowData[0].equals("11")) {
                workTime = rowData[2];
            }
            if (rowData[0].equals("255")) {
                numberLogCode = Integer.valueOf(rowData[2]);
            }
            if (rowData[0].equals("256")) {
                String[] logCodes = rowData[2].split(" ");
                for (int i = 0; i < logCodes.length; i++) {
                    if (logCodes[i].equals("500")) {
                        code500 = i;
                    }
                    if (logCodes[i].equals("501")) {
                        code501 = i;
                    }
                    if (logCodes[i].equals("1")) {
                        code1 = i;
                    }
                    if (logCodes[i].equals("202")) {
                        code202 = i;
                    }
                    if (logCodes[i].equals("301")) {
                        code301 = i;
                    }
                }
            }
            if (rowData[0].equals("257")) {
                String[] logs = rowData[2].split(" ");
                int i = 0;
                while (i < (logs.length / numberLogCode)) {
                    List<Integer> l = new ArrayList<>();
                    l.add(Integer.valueOf(logs[i * numberLogCode + code500]));
                    l.add(Integer.valueOf(logs[i * numberLogCode + code501]));
                    l.add(Integer.valueOf(logs[i * numberLogCode + code1]));
                    l.add(Integer.valueOf(logs[i * numberLogCode + code202]));
                    l.add(Integer.valueOf(logs[i * numberLogCode + code301]));
                    i += 1;
                    logList.add(l);
                }
            }
            if (rowData[0].equals("265")) {
                numTreeCode = Integer.valueOf(rowData[2]);
            }
            if (rowData[0].equals("266")) {
                String[] logCodes = rowData[2].split(" ");
                for (int i = 0; i < logCodes.length; i++) {
                    if (logCodes[i].equals("500")) {
                        code500_1 = i;
                    }
                    if (logCodes[i].equals("2")) {
                        code2 = i;
                    }
                    if (logCodes[i].equals("740")) {
                        code740 = i;
                    }
                    if (logCodes[i].equals("741")) {
                        code741 = i;
                    }
                }
            }
            if (rowData[0].equals("267")) {
                String[] logs = rowData[2].split(" ");
                int i = 0;
                while (i < (logs.length / numTreeCode)) {
                    List<Integer> l = new ArrayList<>();
                    l.add(Integer.valueOf(logs[i * numTreeCode + code500_1]));
                    l.add(Integer.valueOf(logs[i * numTreeCode + code2]));
                    l.add(Integer.valueOf(logs[i * numTreeCode + code740]));
                    l.add(Integer.valueOf(logs[i * numTreeCode + code741]));
                    i += 1;
                    treeList.add(l);
                }
            }
        }

        Map<Integer, List<Integer>> treeMap = new HashMap<>();
        for (List<Integer> tree : treeList) {
            treeMap.put(tree.get(0), tree);
        }

        Map<Integer, List<List<Integer>>> logMap = new HashMap<>();
        for (List<Integer> log : logList) {
            if (logMap.containsKey(log.get(0))) {
                logMap.get(log.get(0)).add(log);
            } else {
                logMap.put(log.get(0), new ArrayList<>(Arrays.asList(log)));
            }
        }

        Set<Integer> tree500 = treeMap.keySet();
        tree500.retainAll(logMap.keySet());

        List<LogData> logData = new ArrayList<>();
        for (int i : tree500) {
            List<Integer> tree = treeMap.get(i);
            List<List<Integer>> logs = logMap.get(i);

            for (List<Integer> log : logs) {
                LogData result = new LogData();
                result.setStemId(tree.get(0));
                result.setSpecies(tree.get(1));
                result.setDbh(tree.get(2));
                result.setType(tree.get(3));
                result.setLogId(log.get(1));
                result.setAssort(log.get(2));
                result.setDiam(log.get(3));
                result.setLength(log.get(4));
                result.setWorkTime(workTime);

                logData.add(result);
            }
        }
        return logData;
    }

    public static List<LogData> analysisXml(MultipartFile f) {
        try {
            String content = new String(f.getBytes());
            Document doc = DocumentHelper.parseText(content);
            List<Node> nodes1 = doc.getRootElement().selectNodes("//StemNumber");
            List<Node> nodes2 = doc.getRootElement().selectNodes("//SpeciesGroupKey");
            List<Node> nodes3 = doc.getRootElement().selectNodes("//DBH");
            List<Node> nodes4 = doc.getRootElement().selectNodes("//LogKey");
            List<Node> nodes5 = doc.getRootElement().selectNodes("//ProductKey");
            List<Node> nodes6 = doc.getRootElement().selectNodes("//LogDiameter");
            List<Node> nodes7 = doc.getRootElement().selectNodes("//LogLength");
            List<Node> nodes8 = doc.getRootElement().selectNodes("//HarvestDate");

            List<LogData> list = new ArrayList<>();
            for (int i = 0; i<nodes1.size(); i++) {
                try {
                    LogData logData = new LogData();
                    logData.setStemId(Integer.valueOf(nodes1.get(i).getText()));
                    logData.setSpecies(Integer.valueOf(nodes2.get(i).getText()));
                    logData.setDbh(Integer.valueOf(nodes3.get(i).getText()));
                    logData.setType(0);
                    logData.setLogId(Integer.valueOf(nodes4.get(i).getText()));
                    logData.setAssort(Integer.valueOf(nodes5.get(i).getText()));
                    logData.setDiam(Integer.valueOf(nodes6.get(i).getText()));
                    logData.setLength(Integer.valueOf(nodes7.get(i).getText()));
                    logData.setWorkTime(nodes8.get(i).getText());
                    list.add(logData);
                } catch (Exception e) {
                    continue;
                }
            }
            return list;
        } catch (Exception e) {
        }
        return null;
    }

    public static ResponseEntity<InputStreamResource> download(String fileName, boolean origin) {
        String route = "static" + "/" + "files/" + "/" + (origin ? "origin/" : "output/");
        String path;
        ResponseEntity<InputStreamResource> response = null;
        try {
            path = route + "/" + fileName;
            ClassPathResource classPathResource = new ClassPathResource(path);
            InputStream inputStream = classPathResource.getInputStream();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.add("Content-Disposition",
                    "attachment; filename="
                            + new String(fileName.getBytes("utf-8"), "utf-8"));
            response = ResponseEntity.ok().headers(headers)
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(new InputStreamResource(inputStream));
        } catch (FileNotFoundException e1) {
        } catch (IOException e) {
        }
        return response;
    }
}
