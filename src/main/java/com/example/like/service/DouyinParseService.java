package com.example.like.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class DouyinParseService {

    /**
     * 从包含抖音分享链接的文本中提取视频播放地址
     * 输入示例: "0.00 推荐看这个👍复制打开抖音👀爱好也是截然不同的😽 # 高品质童装 # 童装新款... https://v.douyin.com/r0biyIy6OeE/ SyG:/ 11/14 u@S.lP"
     *
     * @param text 包含抖音分享链接的文本
     * @return 视频播放地址列表
     */
    public List<String> extractPlayAddrUrlsFromText(String text) throws IOException {
        // 1. 从文本中提取抖音分享链接
        List<String> douyinUrls = extractDouyinUrls(text);

        if (douyinUrls.isEmpty()) {
            return new ArrayList<>();
        }

        // 2. 获取第一个链接的重定向地址
        String redirectUrl = getLocationHeader(douyinUrls.get(0));

        System.out.println("redirectUrl: "+redirectUrl);
        if (redirectUrl == null) {
            return new ArrayList<>();
        }

        // 3. 从重定向URL中提取视频ID
        String videoId = extractVideoId(redirectUrl);

        if (videoId == null) {
            return new ArrayList<>();
        }

        // 4. 获取抖音页面内容
        String pageContent = getDouyinPageContent(videoId);

        // 5. 从页面内容中提取播放地址
        List<String> playAddrUrls = extractPlayAddrUrls(pageContent);

        return playAddrUrls.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 从文本中提取抖音分享链接
     *
     * @param text 包含抖音链接的文本
     * @return 提取到的抖音链接列表
     */
    public static List<String> extractDouyinUrls(String text) {
        List<String> urls = new ArrayList<>();

        if (text == null || text.isEmpty()) {
            return urls;
        }

        // 匹配抖音分享链接的正则表达式
        Pattern pattern = Pattern.compile("https?://v\\.douyin\\.com/[\\w\\d-]+/?");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            urls.add(matcher.group());
        }

        return urls;
    }

    public static String getLocationHeader(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 设置不自动跟随重定向
        connection.setInstanceFollowRedirects(false);
        // 设置请求方法
        connection.setRequestMethod("GET");
        // 连接
        connection.connect();
        // 获取Location头部信息
        String location = connection.getHeaderField("Location");
        // 断开连接
        connection.disconnect();
        return location;
    }

    public static String extractVideoId(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }
        // 使用正则表达式提取视频ID
        Pattern pattern = Pattern.compile("/share/video/(\\d+)/");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static String getDouyinPageContent(String videoId) throws IOException {
        String pageUrl = "https://www.douyin.com/jingxuan?modal_id=" + videoId;
        URL url = new URL(pageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // 设置请求方法
        connection.setRequestMethod("GET");
        // 设置请求头，模拟浏览器访问
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        // 连接
        connection.connect();

        // 读取响应
        StringBuilder content = new StringBuilder();
        try (java.util.Scanner scanner = new java.util.Scanner(connection.getInputStream())) {
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine()).append("\n");
            }
        }

        // 断开连接
        connection.disconnect();

        return content.toString();
    }

    public static List<String> extractPlayAddrUrls(String htmlContent) {
        List<String> playAddrUrls = new ArrayList<>();

        if (htmlContent == null || htmlContent.isEmpty()) {
            return playAddrUrls;
        }

        // 查找包含"douyin"和"video"或"vod"的URL，这应该能找到视频URL
        Pattern urlPattern = Pattern.compile("\"(https?://[^\"]*(?:douyinvod|v[^/]*\\.douyin)[^\"]*)\"");
        Matcher urlMatcher = urlPattern.matcher(htmlContent);

        int count = 0;
        while (urlMatcher.find() && count < 20) { // 限制只显示前20个匹配项
            String url = urlMatcher.group(1);
            // 解码Unicode转义序列
            url = url.replace("\\u002F", "/").replace("\\\"", "\"");
            playAddrUrls.add(url);
            count++;
        }

        // 如果没有找到匹配项，尝试更宽松的模式
        if (playAddrUrls.isEmpty()) {
            Pattern loosePattern = Pattern.compile("\"(https?://[^\"]*douyin[^\"]*\\.(?:mp4|flv|m3u8)[^\"]*)\"");
            Matcher looseMatcher = loosePattern.matcher(htmlContent);

            count = 0;
            while (looseMatcher.find() && count < 20) { // 限制只显示前20个匹配项
                String url = looseMatcher.group(1);
                // 解码Unicode转义序列
                url = url.replace("\\u002F", "/").replace("\\\"", "\"");
                playAddrUrls.add(url);
                count++;
            }
        }

        return playAddrUrls;
    }

    /**
     * 下载视频到当前目录
     *
     * @param videoUrl 视频URL
     * @param filename 保存的文件名
     * @throws IOException 下载过程中可能抛出的IO异常
     */
    public static void downloadVideo(String videoUrl, String filename) throws IOException {
        URL url = new URL(videoUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        connection.setRequestProperty("Referer", videoUrl);

        try (InputStream inputStream = connection.getInputStream();
             OutputStream outputStream = Files.newOutputStream(Paths.get(filename))) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } finally {
            connection.disconnect();
        }
    }
}