package com.chenwenjing.graduationproject.service;

import com.chenwenjing.graduationproject.data.File;

import java.util.List;

public interface FileService {

    List<File> list(int status);

    File get(int fileId);

    List<File> search(String name);

    void insert(File file);

    void like(int userId, int fileId, boolean positive);

    boolean checkLike(int userId, int fileId);

    List<File> listLike(int userId);

    void update(File file);
}
