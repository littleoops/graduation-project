package com.chenwenjing.graduationproject.service.impl;

import com.chenwenjing.graduationproject.data.Order;
import com.chenwenjing.graduationproject.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void insert(Order order) {
        long now = System.currentTimeMillis();
        String sql = "insert into `order` (userId, fileId, status, reason, createdTime, updatedTime) values (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, order.getUserId(), order.getFileId(), 0, order.getReason(), now, now);
    }

    @Override
    public void allow(int orderId) {
        long now = System.currentTimeMillis();
        String sql = "update `order` set status = 1, updatedTime = ? where id = ?";
        jdbcTemplate.update(sql, now, orderId);
    }

    @Override
    public List<Order> list() {
        String sql = "select * from `order` order by updatedTime desc";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public boolean checkOrder(int userId, int fileId) {
        String sql = "select * from `order` where userId=? and fileId=? and status=1";
        List<Order> orders = jdbcTemplate.query(sql, ROW_MAPPER, userId, fileId);
        return !CollectionUtils.isEmpty(orders);
    }

    @Override
    public boolean checkSubmit(int userId, int fileId) {
        String sql = "select * from `order` where userId=? and fileId=?";
        List<Order> orders = jdbcTemplate.query(sql, ROW_MAPPER, userId, fileId);
        return !CollectionUtils.isEmpty(orders);
    }

    private RowMapper<Order> ROW_MAPPER = (rs, row) -> {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setUserId(rs.getInt("userId"));
        order.setFileId(rs.getInt("fileId"));
        order.setStatus(rs.getInt("status"));
        order.setReason(rs.getString("reason"));
        order.setCreatedTime(rs.getLong("createdTime"));
        order.setUpdatedTime(rs.getLong("updatedTime"));
        return order;
    };
}
