package org.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;


public class LLMClient {

    // 1. 從環境變數讀取全新的 OpenAI API Key
    private static final String API_KEY = System.getenv("OPENAI_API_KEY");

    // 2. OpenAI 的標準對話模型 API 端點
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    /**
     * 傳入方法原始碼，回傳 OpenAI 的中文解釋
     */
    public static String askAI(String methodCode) {
        if (API_KEY == null || API_KEY.isBlank()) {
            return " 錯誤：找不到環境變數 OPENAI_API_KEY";
        }

        String promptTemplate = "";
        try {
            promptTemplate = Files.readString(Path.of("prompt.txt"));
        } catch (Exception e) {
            promptTemplate = "預設提示詞..."; // 防呆
        }
        String prompt = promptTemplate + "\n\n" + methodCode;

        // 3. 依照 OpenAI 的規範建構 JSON 請求主體 (model + messages 陣列)
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", "gpt-4o-mini");

        JsonArray messagesArray = new JsonArray();
        JsonObject messageObj = new JsonObject();
        messageObj.addProperty("role", "user");
        messageObj.addProperty("content", prompt);
        messagesArray.add(messageObj);

        requestBody.add("messages", messagesArray);
        String jsonPayload = requestBody.toString();

        // 4. 發送 HTTP POST 請求 (注意：OpenAI 需要 Bearer Token 驗證)
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + API_KEY) // OpenAI 的認證標頭
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();

            // 檢查回應是否包含錯誤區塊
            if (jsonResponse.has("error")) {
                return " OpenAI API 發生錯誤: " + jsonResponse.getAsJsonObject("error").get("message").getAsString();
            }

            // 5. 解析 OpenAI 的回傳結構：choices[0].message.content
            String aiAnswer = jsonResponse.getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content").getAsString();

            return aiAnswer.trim();

        } catch (Exception e) {
            System.err.println(" OpenAI 連線失敗：" + e.getMessage());
            return " AI 解釋生成失敗";
        }
    }
}