package com.chenwenjing.graduationproject.service;

import com.chenwenjing.graduationproject.data.User;

import java.util.List;

public interface UserService {

    User get(int id);

    User getByName(String name);

    User getByNameAndPassword(String name, String password);

    User register(User user);

    List<User> list();

    void updateRole(int userId, int role);
}
