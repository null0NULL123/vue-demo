package com.example.homework.service.serviceImp;

import com.example.homework.mapper.ArticleMapper;
import com.example.homework.mapper.MessageMapper;
import com.example.homework.pojo.Article;
import com.example.homework.pojo.Message;
import com.example.homework.pojo.PageBean;
import com.example.homework.service.MessageService;
import com.example.homework.utils.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public void add(Message message) {
        message.setTimestamp(LocalDateTime.now());
        messageMapper.add(message);
    }

    @Override
    public List<Message> list(Message message) {
        List<Message> as = messageMapper.list(message);
        System.out.println(as);
        System.out.println(message.getRecipient_id());
        return as;
    }

    @Override
    public List<Message> userlist(Message message) {
        return messageMapper.userlist(message);
    }


}
