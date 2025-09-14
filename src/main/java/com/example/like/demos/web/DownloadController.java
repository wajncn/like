package com.example.like.demos.web;

import com.example.like.service.DouyinParseService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

@RequestMapping("download")
@RestController
public class DownloadController {

    @Resource
    private DouyinParseService douyinParseService;

    @PostMapping("/video")
    public void downloadDouyinVideo(@RequestBody Map<String, String> payload, HttpServletResponse response) throws IOException {
        try {
            String text = payload.get("url");
            if (text == null || text.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("URL is required");
                response.getWriter().flush();
                return;
            }

            // Extract playAddr URLs from the text
            List<String> playAddrUrls = douyinParseService.extractPlayAddrUrlsFromText(text);

            if (playAddrUrls.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("No video URLs found in the provided text");
                response.getWriter().flush();
                return;
            }

            // Get the first video URL
            String videoUrl = playAddrUrls.get(0);

            // Set response headers for file download
            response.setContentType("video/mp4");
            response.setHeader("Content-Disposition", "attachment; filename=\"douyin_video.mp4\"");

            // Stream the video directly to the response
            URL url = new URL(videoUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
            connection.setRequestProperty("Referer", videoUrl);

            try (InputStream inputStream = connection.getInputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    response.getOutputStream().write(buffer, 0, bytesRead);
                }
                response.getOutputStream().flush();
            } finally {
                connection.disconnect();
            }
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error downloading video: " + e.getMessage());
            response.getWriter().flush();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Unexpected error: " + e.getMessage());
            response.getWriter().flush();
        }
    }

}
