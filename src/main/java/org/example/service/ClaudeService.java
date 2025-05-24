package org.example.service;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.IOException;

public class ClaudeService {

    private static final String API_KEY = "AIzaSyC34a5nvlW-CXflWbPGLZims8gZY2bb3lI";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;

    private final OkHttpClient client = new OkHttpClient();

    public String generateContent(String systemInstructionText, String userMessageText) throws IOException {
        JSONObject systemInstruction = new JSONObject()
                .put("parts", new JSONArray()
                        .put(new JSONObject().put("text", systemInstructionText)));

        JSONObject userContent = new JSONObject()
                .put("role", "user")
                .put("parts", new JSONArray()
                        .put(new JSONObject().put("text", userMessageText)));

        JSONArray contents = new JSONArray().put(userContent);

        JSONObject requestBodyJson = new JSONObject()
                .put("system_instruction", systemInstruction)
                .put("contents", contents)
                .put("generationConfig", new JSONObject()
                        .put("maxOutputTokens", 1024)
                        .put("temperature", 0.7));

        RequestBody body = RequestBody.create(requestBodyJson.toString(), MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "Ошибка: " + response.code() + " " + response.message() + "\n" + response.body().string();
            }

            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);

            JSONArray candidates = jsonResponse.optJSONArray("candidates");
            if (candidates != null && candidates.length() > 0) {
                JSONObject candidate = candidates.getJSONObject(0);
                JSONObject contentObj = candidate.getJSONObject("content");

                JSONArray parts = contentObj.getJSONArray("parts");

                StringBuilder answerBuilder = new StringBuilder();
                for (int i = 0; i < parts.length(); i++) {
                    JSONObject part = parts.getJSONObject(i);
                    if (part.has("text")) {
                        answerBuilder.append(part.getString("text"));
                    }
                }

                return answerBuilder.toString();
            } else {
                return "Ответ не найден в ответе API";
            }
        }
    }


    public static void main(String[] args) {
        ClaudeService client = new ClaudeService();

        String systemInstruction = "Ты — помощник для людей с ограниченными возможностями здоровья (ОВЗ). Отвечай просто, понятно и с заботой.";
        String userMessage = "Расскажи, какие социальные выплаты положены людям с инвалидностью в Кыргызстане.";

        try {
            String answer = client.generateContent(systemInstruction, userMessage);
            System.out.println("Ответ Gemini:\n" + answer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}