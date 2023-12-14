package com.alfabaykal.telegramcategorytreebot.config;

import com.alfabaykal.telegramcategorytreebot.bot.TelegramCategoryTreeBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Инициализатор бота, регистрирующий бота при запуске приложения.
 */
@Component
@Slf4j
public class BotInitializer {

    /** Бот для управления деревом категорий в Telegram */
    private final TelegramCategoryTreeBot bot;

    /**
     * Конструктор с параметрами
     *
     * @param bot Бот для управления деревом категорий в Telegram
     */
    public BotInitializer(TelegramCategoryTreeBot bot) {
        this.bot = bot;
    }

    /**
     * Метод для инициализации бота при запуске приложения
     *
     * @throws TelegramApiException В случае ошибки при регистрации бота
     */
    @EventListener(ContextRefreshedEvent.class)
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

        try {
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
