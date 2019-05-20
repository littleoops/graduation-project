package com.chenwenjing.graduationproject.service.impl;

import com.chenwenjing.graduationproject.data.File;
import com.chenwenjing.graduationproject.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<File> list(int status) {
        String sql = "select * from file" + (status < 0 ? "" : (" where status = " + status) + " order by updatedTime desc");
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public File get(int fileId) {
        String sql = "select * from file where id = ?";
        return jdbcTemplate.queryForObject(sql, ROW_MAPPER, fileId);
    }

    @Override
    public List<File> search(String name) {
        String sql = String.format("select * from file where name like %s", "%" + name + "%");
        return jdbcTemplate.query(sql, ROW_MAPPER, name);
    }

    @Override
    public void insert(File file) {
        long now = System.currentTimeMillis();
        String sql = "insert into `file` (name, size, originUrl, outputUrl, status, taskId, reporter, createdTime, updatedTime) " +
                " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, file.getName(), file.getSize(), file.getOriginUrl(), file.getOutputUrl(),
                0, file.getTaskId(), file.getReporter(), now, now);
    }

    @Override
    public void update(File file) {
        long now = System.currentTimeMillis();
        String sql = "update file set status = ?, updatedTime = ? where id = ?";
        jdbcTemplate.update(sql, file.getStatus(), now, file.getId());
    }

    @Override
    public boolean checkLike(int userId, int fileId) {
        String sql = "select count(*) from user_file where status = 1 and userId = ? and fileId = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, userId, fileId);
        return count > 0;
    }

    @Override
    public void like(int userId, int fileId, boolean positive) {
        // status: 0 未收藏 1 收藏
        long now = System.currentTimeMillis();
        String sql = "insert into `user_file` (userId, fileId, status, createdTime, updatedTime) values (?, ?, ?, ?, ?) " +
                "on duplicate key update status = ?, updatedTime = ?";
        jdbcTemplate.update(sql, userId, fileId, positive ? 1 : 0, now, now, positive ? 1 : 0, now);
    }

    @Override
    public List<File> listLike(int userId) {
        String sql = "select fileId from user_file where userId = ? and status = 1";
        List<Integer> fileIds = jdbcTemplate.queryForList(sql, Integer.class, userId);
        if (CollectionUtils.isEmpty(fileIds)) {
            return Collections.emptyList();
        }
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
        Map<String, Object> param = new HashMap<>();
        param.put("ids", fileIds);
        sql = "select * from file where status = 1 and id in (:ids)";
        return template.query(sql, param, ROW_MAPPER);
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
