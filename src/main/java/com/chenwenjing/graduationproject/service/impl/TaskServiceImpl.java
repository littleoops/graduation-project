package com.chenwenjing.graduationproject.service.impl;

import com.chenwenjing.graduationproject.data.Task;
import com.chenwenjing.graduationproject.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Task create(Task task) {
        String sql = "insert into task (species, stemCount, logCount, workTime, logAssort, minDBH, maxDBH, minLength, maxLength) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"});
                    ps.setString(1, String.valueOf(task.getSpecies()));
                    ps.setString(2, String.valueOf(task.getStemCount()));
                    ps.setString(3, String.valueOf(task.getLogCount()));
                    ps.setString(4, String.valueOf(task.getWorkTime()));
                    ps.setString(5, String.valueOf(task.getLogAssort()));
                    ps.setString(6, String.valueOf(task.getMinDBH()));
                    ps.setString(7, String.valueOf(task.getMaxDBH()));
                    ps.setString(8, String.valueOf(task.getMinLength()));
                    ps.setString(9, String.valueOf(task.getMaxDBH()));
                    return ps;
                }, keyHolder);
        task.setId(keyHolder.getKey().intValue());
        return task;
    }

    @Override
    public Task get(int taskId) {
        String sql = "select * from task where id = ?";
        return jdbcTemplate.queryForObject(sql, ROW_MAPPER, taskId);
    }

    private RowMapper<Task> ROW_MAPPER = (rs, row) -> {
        Task task = new Task();
        task.setId(rs.getInt("id"));
        task.setSpecies(rs.getString("species"));
        task.setStemCount(rs.getInt("stemCount"));
        task.setLogCount(rs.getInt("logCount"));
        task.setWorkTime(rs.getString("workTime"));
        task.setLogAssort(rs.getString("logAssort"));
        task.setMinDBH(rs.getInt("minDBH"));
        task.setMaxDBH(rs.getInt("maxDBH"));
        task.setMinLength(rs.getInt("minLength"));
        task.setMaxLength(rs.getInt("maxLength"));
        return task;
    };
}
