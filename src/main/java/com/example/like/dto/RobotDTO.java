package com.example.like.dto;

import lombok.Data;

@Data
public class RobotDTO {
    private String key;
    private int count;
    private String lastLog;
    private boolean stopped;
}
