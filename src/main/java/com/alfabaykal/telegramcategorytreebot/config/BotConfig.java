package com.alfabaykal.telegramcategorytreebot.config;

import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационный класс для настройки бота
 */
@Configuration
@Getter
public class BotConfig {
    /** Имя бота */
    @Value("${bot.username}")
    private String botUsername;
    /** Токен бота */
    @Value("${bot.token}")
    private String token;
    /** ID создателя бота */
    @Value("${bot.creator.id}")
    private long creatorID;

    /**
     * Создает и возвращает объект ModelMapper для использования в приложении
     *
     * @return Объект ModelMapper
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
