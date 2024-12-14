package com.example.homework.service;

import com.example.homework.pojo.Message;
import com.example.homework.pojo.PageBean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MessageService {
    void add(Message message);

    List<Message> list(Message message);

    List<Message> userlist(Message message);
}
