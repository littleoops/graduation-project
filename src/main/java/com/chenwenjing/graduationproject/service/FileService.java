package com.chenwenjing.graduationproject.service;

import com.chenwenjing.graduationproject.data.File;

import java.util.List;

public interface FileService {

    List<File> list(int status);

}
