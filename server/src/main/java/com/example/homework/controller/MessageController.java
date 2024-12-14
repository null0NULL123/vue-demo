package com.example.homework.controller;
import com.example.homework.pojo.Message;
import com.example.homework.pojo.Result;
import com.example.homework.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    //添加发送私信内容
    @PostMapping
    public Result<Void> add(@RequestBody Message message) {
        System.out.println(message);
        messageService.add(message);
        return Result.success();
    }

    @PostMapping("/history")
    public Result<List<Message>> list(@RequestBody Message message)
    {
        List<Message> me =  messageService.list(message);

        return Result.success(me);
    }

    @PostMapping("/userlist")
    public Result<Map<String, Object>> userlist(@RequestBody Message message)
    {
        List<Message> me = messageService.userlist(message);
        System.out.println(message);
        Set<Integer> recipientIds = me.stream()
                .map(Message::getRecipient_id)
                .filter(recipientId -> recipientId != message.getSender_id())
                .collect(Collectors.toSet());

        Map<String, Object> result = new HashMap<>();
        result.put("data", new ArrayList<>(recipientIds));

        return Result.success(result);
    }

}
