package com.example.homework.pojo;

import com.example.homework.anno.State;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class Message {

    @NotEmpty
    private Integer sender_id;

    @NotEmpty
    private Integer recipient_id;

    @NotEmpty
    @Pattern(regexp = "^\\S{0,300}$")
    private String content;//私信内容

    private LocalDateTime timestamp;//更新时间
}