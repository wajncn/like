package com.example.like.controller;

import com.example.like.dto.RobotDTO;
import com.example.like.service.LikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequestMapping("like")
@RestController
@Slf4j
public class LikeController {

    private final Map<String, LikeService> map = new ConcurrentHashMap<>();

    @GetMapping("/log")
    public List<RobotDTO> list() {
        List<RobotDTO> list = new ArrayList<>();
        for (Map.Entry<String, LikeService> stringLikeEntry : map.entrySet()) {
            RobotDTO robotDTO = new RobotDTO();
            robotDTO.setKey(stringLikeEntry.getKey());
            robotDTO.setCount(stringLikeEntry.getValue().getCount());
            robotDTO.setStopped(stringLikeEntry.getValue().isStopped());
            list.add(robotDTO);
        }
        return list;
    }

    @GetMapping("/stop")
    public int stop(@RequestParam String key) {
        LikeService like = map.get(key);
        if (like == null) {
            return 0;
        }
        like.stop();
        return 1;
    }

    @PostMapping(value = "/curl", consumes = "text/plain;charset=UTF-8")
    public String hello(@RequestBody String curl) {
        final String key = DigestUtils.md5DigestAsHex(curl.trim().getBytes());
        if (map.containsKey(key)) {
            return "已存在";
        }
        try {
            LikeService like = new LikeService(curl, key);
            final String r = like.curlToUnirest(curl);
            map.put(key, like);
            Thread thread = new Thread(like);
            like.setThread(thread);
            thread.start();
            return r;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            map.remove(key);
            return e.getMessage();
        }
    }
}
