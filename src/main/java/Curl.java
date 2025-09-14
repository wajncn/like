import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Curl {

    public static void main(String[] args) throws IOException, InterruptedException {
        Process exec = Runtime.getRuntime().exec("curl 'https://live.douyin.com/webcast/room/like/?aid=6383&app_name=douyin_web&live_id=1&device_platform=web&language=zh-CN&enter_from=web_homepage_follow&cookie_enabled=true&screen_width=1440&screen_height=900&browser_language=zh-CN&browser_platform=MacIntel&browser_name=Chrome&browser_version=140.0.0.0&room_id=7549766654702029607&count=2&msToken=9N00kOly9kFOhhuQDuzfaP2wbe51bSUjFYFemiBMs-MoEGrClQGTY-OwIJLPzgMkakcUSNnLRGI_-bgv4qEZ6WDLaj8u73uSGIsO-UQb8Qc96r11altIfBU-mWYQ3nQ9s6LQBFF12O_dF8cpzkmVmj-RV2PmM1F5kPUDK4qLn3z0&a_bogus=dJ0nDqy7dd5VKd%2FbYOpwH6qUAexArPuyTTTKWiKTenaNO7zbamNUgJ27bqu1AG8PPWp0wep7efWAGDfP0sIe2Kxpwmkfuusjs459Ig8o%2Fqq2TzJsDHDTS84zLwse8RsLaA9ail8RXsBr1dclVr5MAQpG95TH-mbpSqMRd2uyyEAhDW8khx-wO9gpP6raUaAt&verifyFp=verify_mfj83xbg_PcTHJ5Lc_7PPe_49JU_BhBq_wM6NZUc0v7Z6&fp=verify_mfj83xbg_PcTHJ5Lc_7PPe_49JU_BhBq_wM6NZUc0v7Z6' \\\n" +
                "  -H 'accept: application/json, text/plain, */*' \\\n" +
                "  -H 'accept-language: zh-CN,zh;q=0.9' \\\n" +
                "  -H 'cache-control: no-cache' \\\n" +
                "  -H 'content-type: application/json' \\\n" +
                "  -b 'enter_pc_once=1; UIFID_TEMP=94323c0887b37f94f50ac5417d2d415b74fcf5f1106c230e40c326c63b00b663a50041d303b06192c9141a7fbc20264c9f44caf8a1b4473e501cf4a9ab7c125405a5b5a46f3a53f6ca670d30ebe15ab132426f6518683bafbfd0fec6462c963be38d9fc988edb86ad45f2744d969140d; hevc_supported=true; home_can_add_dy_2_desktop=%220%22; stream_recommend_feed_params=%22%7B%5C%22cookie_enabled%5C%22%3Atrue%2C%5C%22screen_width%5C%22%3A1440%2C%5C%22screen_height%5C%22%3A900%2C%5C%22browser_online%5C%22%3Atrue%2C%5C%22cpu_core_num%5C%22%3A2%2C%5C%22device_memory%5C%22%3A8%2C%5C%22downlink%5C%22%3A10%2C%5C%22effective_type%5C%22%3A%5C%224g%5C%22%2C%5C%22round_trip_time%5C%22%3A100%7D%22; strategyABtestKey=%221757825826.626%22; passport_csrf_token=01611e2f65d3d38cecdaaa338c46bdcf; passport_csrf_token_default=01611e2f65d3d38cecdaaa338c46bdcf; volume_info=%7B%22isUserMute%22%3Afalse%2C%22isMute%22%3Afalse%2C%22volume%22%3A0.5%7D; __security_mc_1_s_sdk_crypt_sdk=f25b29dd-4400-b939; bd_ticket_guard_client_web_domain=2; gulu_source_res=eyJwX2luIjoiZWEzYjMwNTQ5YWI3NTZiMzk4YzRjMmJmMGQ4NDY4YjJkNDZlOTFiNWNlMGMxZGE0YTZiNWYyZGQ4ZTU1NWQyNiJ9; sdk_source_info=7e276470716a68645a606960273f276364697660272927676c715a6d6069756077273f276364697660272927666d776a68605a607d71606b766c6a6b5a7666776c7571273f275e58272927666a6b766a69605a696c6061273f27636469766027292762696a6764695a7364776c6467696076273f275e582729277672715a646971273f2763646976602729277f6b5a666475273f2763646976602729276d6a6e5a6b6a716c273f2763646976602729276c6b6f5a7f6367273f27636469766027292771273f2733353436363d30373d32303234272927676c715a75776a716a666a69273f2763646976602778; bit_env=xnYZ9oyAD5L8OvXXkJ-VGzaXxA3KuyZsHhnmbIgtG508lxiCzufI53_-aMq2k3o_d0qaDgaraOMrIkwKKNNCWVfgikDhlZJzWjsEC9L-MqeRUpo4JdqfCHDDRHR0fDz8RbdATNhXM7xML3XxperKpPzAf-HXu98LV5l0tLxYq-zqeaopH3bFRQTrYyMnSwhggGBmLreJOw7gaYTuYeCTIiRB1UlZWcafk08bmW4Lz8JPFbK_hLp98M09SKldYKZLQJyfO7mAI803uh7F5tADuJ2MxScEK-8M_aNGHHb3x37WIob73B53J-4Ub1xZr9tgx2EyvrKaySpaRf7jtMJZz8HmP7aE6EVkSmQDG0DNJcneLtbom09D8lVAKrF8xp52VHhUvSzOGSNRfL39gnf5QFRdWxf-VDjhfeTKhHGMybZ0xmbKL6H7CLCmUAFxIKqsC5bNdiMSpIU9ozi51ZXCqckgmTBPSSmtSNpxkG1yA-1ZcjPf6SY8WyzAfQTD0OeNKLiWpeX8PVruy9MGgRo8vXhW5jXjwQc9gLm8kvEuOks%3D; passport_auth_mix_state=azjr6f4oihevxgiwf56l4cqde12qpb0y; passport_assist_user=CkrecIS5I3hx04TPznfpd_oeCz2D50drLh3_szJjKahJw_iqflKuwarqGGUnAsZkqM5ge2rZFHcmFmDzFnd2qK4AQ4qW5sk094FfbxpKCjwAAAAAAAAAAAAAT3lda9zn1G9EgczMeifEISzpFHZpD2h3Z_8alnyf85tZoFT3WiO0oFvVY9JtN2-WJAoQopL8DRiJr9ZUIAEiAQP5u30N; n_mh=A4WbVeSZLZxOea-aLg0MLVlT98YaGzPg1hQ0OgFUCE8; sid_guard=278e992b81c60ce9acd4ba943cfa0d47%7C1757825856%7C5184000%7CThu%2C+13-Nov-2025+04%3A57%3A36+GMT; uid_tt=910e74c57a3140fe03a18ccc05b9cc5f; uid_tt_ss=910e74c57a3140fe03a18ccc05b9cc5f; sid_tt=278e992b81c60ce9acd4ba943cfa0d47; sessionid=278e992b81c60ce9acd4ba943cfa0d47; sessionid_ss=278e992b81c60ce9acd4ba943cfa0d47; is_staff_user=false; sid_ucp_v1=1.0.0-KDE4M2U3N2MyMzdlNjEyM2Q1N2QyN2U4YmFlMDU2MjMwMGMyZjE0OWMKIAjb7cCu18xCEMCWmcYGGO8xIAwwtLrdqgY4B0D0B0gEGgJsZiIgMjc4ZTk5MmI4MWM2MGNlOWFjZDRiYTk0M2NmYTBkNDc; ssid_ucp_v1=1.0.0-KDE4M2U3N2MyMzdlNjEyM2Q1N2QyN2U4YmFlMDU2MjMwMGMyZjE0OWMKIAjb7cCu18xCEMCWmcYGGO8xIAwwtLrdqgY4B0D0B0gEGgJsZiIgMjc4ZTk5MmI4MWM2MGNlOWFjZDRiYTk0M2NmYTBkNDc; login_time=1757825856592; _bd_ticket_crypt_cookie=9e8a5a5982773764d723cb22703e9df0; __security_mc_1_s_sdk_sign_data_key_web_protect=e0fb7dad-48f7-82c3; __security_mc_1_s_sdk_cert_key=03ddf2cf-4869-9b81; __security_server_data_status=1; UIFID=94323c0887b37f94f50ac5417d2d415b74fcf5f1106c230e40c326c63b00b663a50041d303b06192c9141a7fbc20264c9f44caf8a1b4473e501cf4a9ab7c12542350493e6e742f74eb8e70d818df2cbc207ff533d8bd508c5520856f4ddcef9371e7b996cdeb75fa28a588aada43f43b889daf0a118d977a2ee928dc8d1d0e404c0871f71df4cfdfae24caf845be1950223c772ad45de48cec808bdf88ba305066a6cdbb10540309eaff4a6d298572adda6295d23415cd8d4f4e8d8a9388c160; SelfTabRedDotControl=%5B%7B%22id%22%3A%227458240346309265418%22%2C%22u%22%3A98%2C%22c%22%3A0%7D%5D; is_dash_user=1; bd_ticket_guard_client_data=eyJiZC10aWNrZXQtZ3VhcmQtdmVyc2lvbiI6MiwiYmQtdGlja2V0LWd1YXJkLWl0ZXJhdGlvbi12ZXJzaW9uIjoxLCJiZC10aWNrZXQtZ3VhcmQtcmVlLXB1YmxpYy1rZXkiOiJCQmRSQW1UWlBTUzdsREtoeUVsYkFaTWlVSjQ3aTU2d1hwTGx0UUZoZ3ZHMGZHOEMvUEhHdjhDK1ZBSXZPWDkxWmJCY1poQ3VYLzdIRTdNb2tNM24rOUE9IiwiYmQtdGlja2V0LWd1YXJkLXdlYi12ZXJzaW9uIjoyfQ%3D%3D; WallpaperGuide=%7B%22showTime%22%3A0%2C%22closeTime%22%3A0%2C%22showCount%22%3A0%2C%22cursor1%22%3A10%2C%22cursor2%22%3A2%7D; FOLLOW_LIVE_POINT_INFO=%22MS4wLjABAAAAM-krtvGACo8TW0TFA8R0Ex6K9KPe3NieCibNwiZrY2A%2F1757865600000%2F1757825867601%2F1757825861131%2F0%22; publish_badge_show_info=%220%2C0%2C0%2C1757825878991%22; odin_tt=7e91ddb45a9e829310695fc5093a3e9b5ec9d7c9acc1080d809cf99a61a243091e0284a357983aae139aa3b5d08291b079a3f96d7738581dd41addc5d03c681e25a8678a50316d9eba9424ad66b9c446; live_use_vvc=%22false%22; ttwid=1%7CqR7Wdxo8BMVukj2lDNWI1B5KNXPDjkZ5M03qTxfXZUc%7C1757825891%7Ce3b173918387a124a0d12947f2526d2a3940eee0dc196e56ac1e05f673e4278b; IsDouyinActive=true; biz_trace_id=678b1d69; session_tlb_tag=sttt%7C10%7CJ46ZK4HGDOms1LqUPPoNR__________WiuYBJugfYvGAetDNz7IdShH2SUjeLeFR_iyY83KmnFY%3D' \\\n" +
                "  -H 'origin: https://www.douyin.com' \\\n" +
                "  -H 'pragma: no-cache' \\\n" +
                "  -H 'priority: u=1, i' \\\n" +
                "  -H 'referer: https://www.douyin.com/' \\\n" +
                "  -H 'sec-ch-ua: \"Chromium\";v=\"140\", \"Not=A?Brand\";v=\"24\", \"Google Chrome\";v=\"140\"' \\\n" +
                "  -H 'sec-ch-ua-mobile: ?0' \\\n" +
                "  -H 'sec-ch-ua-platform: \"macOS\"' \\\n" +
                "  -H 'sec-fetch-dest: empty' \\\n" +
                "  -H 'sec-fetch-mode: cors' \\\n" +
                "  -H 'sec-fetch-site: same-site' \\\n" +
                "  -H 'user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36' \\\n" +
                "  --data-raw '{}'");

        BufferedReader reader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
        String line;
        StringBuilder output = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        int exitCode = exec.waitFor();

        System.out.println("Exit Code: " + exitCode);
        System.out.println("Response:");
        System.out.println(output.toString());

        reader.close();
    }
}
