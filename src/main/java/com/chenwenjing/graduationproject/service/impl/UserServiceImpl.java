package com.chenwenjing.graduationproject.service.impl;

import com.chenwenjing.graduationproject.data.User;
import com.chenwenjing.graduationproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public User getByName(String name) {
        String sql = "select * from user where name = ?";
        return jdbcTemplate.queryForObject(sql, ROW_MAPPER, name);
    }

    private RowMapper<User> ROW_MAPPER = (rs, row) -> {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        return user;
    };
}
