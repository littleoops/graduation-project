package com.chenwenjing.graduationproject.service.impl;

import com.chenwenjing.graduationproject.data.File;
import com.chenwenjing.graduationproject.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<File> list(int status) {
        String sql = "select * from file" + (status < 0 ? "" : (" where status = " + status));
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    private RowMapper<File> ROW_MAPPER = (rs, row) -> {
        File file = new File();
        file.setId(rs.getInt("id"));
        file.setName(rs.getString("name"));
        file.setSize(rs.getInt("size"));
        file.setOriginUrl(rs.getString("originUrl"));
        file.setOutputUrl(rs.getString("outputUrl"));
        file.setStatus(rs.getInt("status"));
        file.setTaskId(rs.getInt("taskId"));
        file.setReporter(rs.getString("reporter"));
        file.setAuditor(rs.getString("auditor"));
        file.setCreatedTime(rs.getLong("createdTime"));
        file.setUpdatedTime(rs.getLong("updatedTime"));
        return file;
    };
}
