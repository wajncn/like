package com.example.like.service;

import kong.unirest.Unirest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@RequiredArgsConstructor
public class LikeService implements Runnable {
    private final String curl;
    private final String key;
    private int count;
    private volatile boolean stopped = false;
    @Setter
    private Thread thread;

    @Getter
    private String lastLog;
    public void stop() {
        stopped = true;
        if (thread != null) {
            thread.interrupt();
        }
    }

    @Override
    public void run() {
        while (!stopped && !Thread.currentThread().isInterrupted()) {
            try {
                final String body = curlToUnirest(curl);
                lastLog = body;
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
        System.out.println("done");
    }

    public String curlToUnirest(String curlCommand) {
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