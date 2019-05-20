package com.chenwenjing.graduationproject.service.impl;

import com.chenwenjing.graduationproject.data.User;
import com.chenwenjing.graduationproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public User get(int id) {
        String sql = "select * from user where id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, ROW_MAPPER, id);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public User getByName(String name) {
        String sql = "select * from user where name = ?";
        try {
            return jdbcTemplate.queryForObject(sql, ROW_MAPPER, name);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public User register(User user) {
        long now = System.currentTimeMillis();
        String sql = "insert into user (name, password, role, email, createdTime, updatedTime) values (?, ?, 0, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getName(), user.getPassword(), user.getEmail(), now, now);
        return user;
    }

    @Override
    public User getByNameAndPassword(String name, String password) {
        String sql = "select * from user where name = ? and password = ?";
        try {
            return jdbcTemplate.queryForObject(sql, ROW_MAPPER, name, password);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<User> list() {
        String sql = "select * from user where role != 100";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public void updateRole(int userId, int role) {
        String sql = "update user set role = ? where id = ?";
        jdbcTemplate.update(sql, role, userId);
    }

    private RowMapper<User> ROW_MAPPER = (rs, row) -> {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getInt("role"));
        user.setEmail(rs.getString("email"));
        user.setCreatedTime(rs.getLong("createdTime"));
        user.setUpdatedTime(rs.getLong("updatedTime"));
        return user;
    };
}
