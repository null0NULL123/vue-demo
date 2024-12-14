package com.example.homework.mapper;

import com.example.homework.pojo.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageMapper {

    @Insert("INSERT INTO messages (sender_id, recipient_id, content, timestamp) " +
            "VALUES (#{sender_id}, #{recipient_id}, #{content}, #{timestamp})")
    void add(Message message);

//    @Select("select * from messages "+
//            "WHERE sender_id = #{sender_id} AND recipient_id = #{recipient_id} " +
//            "ORDER BY timestamp DESC")
//    List<Message> list(Message message);

    List<Message> list(Message message);

    List<Message> userlist(Message message);
}
