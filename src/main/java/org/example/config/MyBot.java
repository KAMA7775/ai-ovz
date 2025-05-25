package org.example.config;

import org.example.config.BotConfig;
import org.example.service.ClaudeService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyBot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return BotConfig.BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BotConfig.BOT_TOKEN;
    }

    private final Map<Long, Boolean> chatMode = new HashMap<>();
    private final ClaudeService claudeService = new ClaudeService();

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            // Если пользователь в режиме чата — отправляем запрос в ИИ
            if (chatMode.getOrDefault(chatId, false) && !messageText.startsWith("/")) {
                try {
                    String response = claudeService.generateContent(
                            "Сиз мүмкүнчүлүгү чектелген адамдардын жардамчысысыз. Жөнөкөй, так жана кылдаттык менен жооп бер.", messageText);
                    sendTextMessage(chatId, response);
                } catch (IOException e) {
                    sendTextMessage(chatId, "ИИ менен байланышта ката кетти.");
                    e.printStackTrace();
                }
                return;
            }

            // Команды
            switch (messageText) {
                case "/start":
                    sendTextMessage(chatId,
                            "Салам! 👋\n\n" +
                                    "Бул бот — биздин санариптик платформабыздын жардамчысы.\n" +
                                    "Жаңылыктарды окуй аласың, пайдалуу маалымат аласың жана ИИ менен сүйлөшө аласың.\n\n" +
                                    "Көбүрөөк маалымат үчүн /help деп жазыңыз.");
                    chatMode.put(chatId, false);
                    break;

                case "/help":
                    sendTextMessage(chatId,
                            "🤖 Буйруктардын тизмеси:\n\n" +
                                    "/start – Бот тууралуу маалымат\n" +
                                    "/help – Колдонуу боюнча жардам\n" +
                                    "/chat –  Жасалма Интеллект менен баарлашууну баштоо");
                    chatMode.put(chatId, false);
                    break;

                case "/chat":
                    sendTextMessage(chatId,
                            "🧠 Сиз Жасалма Интеллект менен баарлашууну баштадыңыз.\nСуранычыңызды жазыңыз:");
                    chatMode.put(chatId, true);
                    break;

                default:
                    sendTextMessage(chatId,
                            "Түшүнүксүз буйрук. /help деп жазыңыз жардам алуу үчүн.");
                    chatMode.put(chatId, false);
                    break;
            }
        }
    }

    private void sendTextMessage(long chatId, String response) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(response);
        message.setParseMode("Markdown"); // Можно также использовать "HTML"

        try {
            execute(message); // Метод из TelegramLongPollingBot
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }



}
