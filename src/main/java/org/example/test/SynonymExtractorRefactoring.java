package org.example.test;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Controller
public class SynonymExtractorRefactoring {
    // Naver API credentials
    private static final String clientId = "_zUWHyHj6WVWPNWQK2aM";
    private static final String clientSecret = "1PRURhYEAZ";

    @GetMapping("/test2")
    public ResponseEntity<String> saveData(@RequestParam String word) {
        System.out.println("##########################");
        System.out.println("Received parameter: " + word);
        System.out.println("##########################");

        extractAndSaveSynonyms(word);

        return ResponseEntity.ok(extractAndSaveSynonyms(word));
    }

    // Extract and save synonyms
    public String extractAndSaveSynonyms(String word) {
        String synonyms = getSynonyms(word);
        if (synonyms.length() > 0) {
            saveSynonymsToDb(word, synonyms);
            return "동의어 저장 완료: " + word + " - " + synonyms;
        } else {
            return "동의어를 찾을 수 없습니다: " + word;
        }
    }

    // Step 1: Get synonyms from the Naver API
    public static String getSynonyms(String word) {
        try {
            word = URLEncoder.encode(word, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패",e);
        }

        String apiURL = "https://openapi.naver.com/v1/search/encyc.json?query=" + word;    // JSON 결과

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        //String responseBody = get(param, apiURL, requestHeaders);

        HttpURLConnection con = connect(apiURL);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(word, con.getInputStream());
            } else { // 오류 발생
                return readBody(word, con.getInputStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(String param, InputStream body) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(body));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject jsonResponse = new JSONObject(response.toString());
        JSONArray items = jsonResponse.getJSONArray("items");

        String synonymsStr = "";

        // This is an example; you may need to adjust it according to the actual API response
        if (!items.isEmpty()) {
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                if (item.has("title")) {
                    synonymsStr = Jsoup.parse(item.getString("title")).text();
                    return synonymsStr;
                    //System.out.println("param :: " + param);
                    //if(!item.getString("title").contains(param)) {
                    //  System.out.println("item is not contain param");
                    // synonymsStr += item.getString("title") + ",";
                    //}
                    //System.out.println("item has title");
                    //System.out.println("synonymsStr : " + synonymsStr);
                }
            }
            //return synonymsStr;
        }
        // 유사어 없으면 공백 리턴
        return synonymsStr;
    }

    // Step 2: Connect to the MySQL database
    public static Connection connectToDb() {
        String jdbcUrl = "jdbc:mysql://192.168.0.110:3306/lsh_test";
        String dbUser = "dev";
        String dbPassword = "dev";
        try {
            return DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Step 3: Save synonyms to the database
    public static void saveSynonymsToDb(String word, String synonyms) {
        Connection conn = connectToDb();
        if (conn != null) {
            String insertQuery = "INSERT INTO TM_SIMLR_TRML (ORGINL_TRML, SIMILR_TRML) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                //for (String synonym : synonyms) {
                    pstmt.setString(1, word);
                    pstmt.setString(2, synonyms);
                    pstmt.executeUpdate();
                //}
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}