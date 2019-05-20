package com.chenwenjing.graduationproject.service;

import com.chenwenjing.graduationproject.data.Task;

public interface TaskService {

    Task create(Task task);

    Task get(int taskId);
}
