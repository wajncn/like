/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.like.demos.web;

import com.example.like.RobotDTO;
import kong.unirest.Unirest;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class BasicController {

    private static final Logger log = LoggerFactory.getLogger(BasicController.class);
    private final Map<String, Like> map = new ConcurrentHashMap<>();

    @GetMapping("log")
    public List<RobotDTO> list() {
        List<RobotDTO> list = new ArrayList<>();
        for (Map.Entry<String, Like> stringLikeEntry : map.entrySet()) {
            RobotDTO robotDTO = new RobotDTO();
            robotDTO.setKey(stringLikeEntry.getKey());
            robotDTO.setCount(stringLikeEntry.getValue().count);
            robotDTO.setStopped(stringLikeEntry.getValue().stopped);
            list.add(robotDTO);
        }
        return list;
    }

    @GetMapping("stop")
    public int stop(@RequestParam String key) {
        Like like = map.get(key);
        if(like == null) {
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
        final String r = curlToUnirest(curl);
        try {
            Like like = new Like(curl,key);
            map.put(key, like);
            Thread thread = new Thread(like);
            like.setThread(thread);
            thread.start();
        } catch (Exception e) {
            map.remove(key);
        }
        return r;
    }



    private static class Like implements Runnable {
        private final String curl;
        private final String key;
        private int count;
        private volatile boolean stopped = false;
        @Setter
        private Thread thread;

        public void stop() {
            stopped = true;
            if (thread != null) {
                thread.interrupt();
            }
        }

        private Like(String curl, String key) {
            this.curl = curl;
            this.key = key;
        }

        @Override
        public void run() {
            while (!stopped && !Thread.currentThread().isInterrupted()) {
                try {
                    final String body = curlToUnirest(curl);
                    log.info("{} {}", key, body);
                    if (body.contains("手速太快了")) {
                        Thread.sleep(1000 * 60 * 5);
                        continue;
                    }
                    if (body.contains("直播已结束")) {
                        stop();
                        break;
                    }
                    count++;
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    log.info("Thread interrupted: {}", key);
                    break;
                } catch (Exception e) {
                    log.info("Exception:", e);
                }
            }
        }
    }

    private static String curlToUnirest(String curlCommand) {
        try {
            String url = "";
            String body = "{}";

            String[] lines = curlCommand.split("\\\\\n");

            kong.unirest.HttpRequestWithBody request = null;
            for (String line : lines) {
                line = line.trim();
                if (line.startsWith("curl")) {
                    String urlPart = line.substring(line.indexOf("'") + 1, line.lastIndexOf("'"));
                    url = urlPart;
                    request = Unirest.post(url);
                } else if (line.startsWith("--header")) {
                    String headerValue = line.substring(line.indexOf("'") + 1, line.lastIndexOf("'"));
                    String[] headerParts = headerValue.split(":", 2);
                    if (headerParts.length == 2 && request != null) {
                        String headerName = headerParts[0].trim();
                        String headerVal = headerParts[1].trim();
                        request.header(headerName, headerVal);
                    }
                } else if (line.startsWith("--data")) {
                    String dataValue = line.substring(line.indexOf("'") + 1, line.lastIndexOf("'"));
                    body = dataValue;
                }
            }

            if (request != null) {
                String response = request.body(body).asString().getBody();
                return response;
            } else {
                return "无法解析URL";
            }

        } catch (Exception e) {
            return "执行请求时出错: " + e.getMessage();
        }
    }

}
