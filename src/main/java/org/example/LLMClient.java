package org.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LLMClient {

    private static final String API_KEY = "AQ.Ab8RN6Ib_BtGBfLBxnjTM4LrCik9Kq2zJby-SQQq5p-spycXtw";

    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=" + API_KEY;

    /**
     * 傳入方法原始碼，回傳 AI 的中文解釋
     */
    public static String askAI(String methodCode) {
        String prompt = "你是一個資深的 Java 工程師。請用「一句話（繁體中文，限50字以內）」解釋以下這段程式碼的功能。不要講廢話，不要包含 Markdown 語法，直接給解釋：\n\n" + methodCode;

        // 打包 JSON (保持不變)
        JsonObject textPart = new JsonObject();
        textPart.addProperty("text", prompt);
        JsonArray partsArray = new JsonArray();
        partsArray.add(textPart);
        JsonObject contentObj = new JsonObject();
        contentObj.add("parts", partsArray);
        JsonArray contentsArray = new JsonArray();
        contentsArray.add(contentObj);
        JsonObject requestBodyObj = new JsonObject();
        requestBodyObj.add("contents", contentsArray);
        String jsonPayload = requestBodyObj.toString();

        int maxRetries = 3; // 最大重試次數
        int retryDelay = 4000; // 失敗後等待 4 秒再重試

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(API_URL))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();

                // 檢查是否發生錯誤
                if (jsonResponse.has("error")) {
                    String errorMsg = jsonResponse.getAsJsonObject("error").get("message").getAsString();

                    // 如果是伺服器高負載或流量限制，且還沒超過最大重試次數，就進行重試
                    if ((errorMsg.contains("high demand") || errorMsg.contains("quota")) && attempt < maxRetries) {
                        System.out.println(" 伺服器忙碌中，將於 " + (retryDelay/1000) + " 秒後進行第 " + attempt + " 次重試...");
                        Thread.sleep(retryDelay);
                        continue; // 跳過本次，進入下一次迴圈重試
                    }
                    return " AI API 發生錯誤: " + errorMsg;
                }

                // 成功拿到解析，直接回傳
                return jsonResponse.getAsJsonArray("candidates")
                        .get(0).getAsJsonObject()
                        .getAsJsonObject("content")
                        .getAsJsonArray("parts")
                        .get(0).getAsJsonObject()
                        .get("text").getAsString().trim();

            } catch (Exception e) {
                if (attempt == maxRetries) {
                    return " 連線失敗：" + e.getMessage();
                }
                try { Thread.sleep(retryDelay); } catch (Exception ignored) {}
            }
        }
        return " AI 解釋生成失敗（伺服器持續過載）";
    }
}