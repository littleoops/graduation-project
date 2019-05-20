package com.chenwenjing.graduationproject.service;

import com.chenwenjing.graduationproject.data.Order;

import java.util.List;

public interface OrderService {

    void insert(Order order);

    void allow(int orderId);

    boolean checkOrder(int userId, int fileId);

    boolean checkSubmit(int userId, int fileId);

    List<Order> list();
}
