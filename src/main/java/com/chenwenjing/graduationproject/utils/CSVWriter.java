package com.chenwenjing.graduationproject.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class CSVWriter {

    /**
     * 写CSV文件
     * @param csvFile csv文件名称
     * @param fileHeader 文件头
     * @param content 内容
     * @throws IOException
     */
    public static void write(final String csvFile, final String[] fileHeader, List<String[]> content)
            throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFile));
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(fileHeader));

        for (String[] c : content) {
            csvPrinter.printRecord(Arrays.asList(c));
        }
        csvPrinter.flush();
    }
}
