package com.alfabaykal.telegramcategorytreebot.bot;

import com.alfabaykal.telegramcategorytreebot.config.BotConfig;
import com.alfabaykal.telegramcategorytreebot.exception.CategoryAlreadyExistException;
import com.alfabaykal.telegramcategorytreebot.exception.CategoryNotFoundException;
import com.alfabaykal.telegramcategorytreebot.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

/**
 * Телеграм-бот для управления деревом категорий на основе Spring Boot и PostgreSQL
 */
@Component
@Slf4j
public class TelegramCategoryTreeBot extends AbilityBot {

    private final BotConfig botConfig;
    private final CategoryService categoryService;

    /**
     * Конструктор для TelegramCategoryTreeBot
     *
     * @param botConfig       Конфигурационные свойства бота
     * @param categoryService Сервис для управления деревом категорий
     */
    public TelegramCategoryTreeBot(BotConfig botConfig, CategoryService categoryService) {
        super(botConfig.getToken(), botConfig.getBotUsername());
        this.botConfig = botConfig;
        this.categoryService = categoryService;

        // Инициализация команд бота
        List<BotCommand> botCommands = new ArrayList<>();
        botCommands.add(new BotCommand(start().name(), start().info()));
        botCommands.add(new BotCommand(viewTree().name(), viewTree().info()));
        botCommands.add(new BotCommand(addElement().name(), addElement().info()));
        botCommands.add(new BotCommand(addChildElement().name(), addChildElement().info()));
        botCommands.add(new BotCommand(removeElement().name(), removeElement().info()));
        botCommands.add(new BotCommand(help().name(), help().info()));

        try {
            this.execute(new SetMyCommands(botCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bots command list: " + e.getMessage());
        }

    }

    @Override
    public long creatorId() {
        return botConfig.getCreatorID();
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    @Override
    public void onClosing() {
        super.onClosing();
    }

    // Описание команд бота

    /**
     * Команда "/start" для приветствия пользователя
     *
     * @return Ability для команды "/start"
     */
    public Ability start() {
        return Ability
                .builder()
                .name("start")
                .info("Say hello!")
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> silent.send("Hello " + ctx.user().getFirstName() + "! Nice to meet you!", ctx.chatId()))
                .build();
    }

    /**
     * Команда "/view_tree" для просмотра всех элементов дерева
     *
     * @return Ability для команды "/view_tree"
     */
    public Ability viewTree() {
        return Ability
                .builder()
                .name("view_tree")
                .info("View all elements")
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> silent.send(categoryService.viewTree(), ctx.chatId()))
                .build();
    }

    /**
     * Команда "/add_element" для добавления новой корневой категории
     *
     * @return Ability для команды "/add_element"
     */
    public Ability addElement() {
        return Ability
                .builder()
                .name("add_element")
                .info("Add category element")
                .input(1)
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> {
                    String elementName = ctx.arguments()[0];
                    try {
                        categoryService.addElement(elementName);
                        silent.send("Element added successfully!", ctx.chatId());
                    } catch (CategoryAlreadyExistException e) {
                        log.error("Error adding element: " + e.getMessage(), e);
                        silent.send("Category already exists!", ctx.chatId());
                    }
                })
                .build();
    }

    /**
     * Команда "/add_child_element" для добавления новой подкатегории
     *
     * @return Ability для команды "/add_child_element"
     */
    public Ability addChildElement() {
        return Ability
                .builder()
                .name("add_child_element")
                .info("Add child category element")
                .input(2)
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> {
                    String parentName = ctx.arguments()[0];
                    String childName = ctx.arguments()[1];
                    try {
                        categoryService.addChildElement(parentName, childName);
                        silent.send("Element added successfully!", ctx.chatId());
                    } catch (CategoryAlreadyExistException e) {
                        log.error("Error adding element: " + e.getMessage(), e);
                        silent.send("Child category already exists!", ctx.chatId());
                    }
                })
                .build();
    }

    /**
     * Команда "/remove_element" для удаления категории
     *
     * @return Ability для команды "/remove_element"
     */
    public Ability removeElement() {
        return Ability
                .builder()
                .name("remove_element")
                .info("Remove category element")
                .input(1)
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> {
                    String elementName = ctx.arguments()[0];
                    try {
                        categoryService.removeElement(elementName);
                        silent.send("Element removed successfully!", ctx.chatId());
                    } catch (CategoryNotFoundException e) {
                        log.error("Error deleting element: " + e.getMessage(), e);
                        silent.send("Category doesn't exists!", ctx.chatId());
                    }
                })
                .build();
    }

    /**
     * Команда "/help" для отображения списка доступных команд и их описания
     *
     * @return Ability для команды "/help"
     */
    public Ability help() {
        return Ability
                .builder()
                .name("help")
                .info("Show available commands and their descriptions")
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> {
                    String helpMessage = """
                            Available commands:
                            /start - Say hello!
                            /view_tree - View all elements
                            /add_element <name> - Add category element
                            /add_child_element <parent_name> <child_name> - Add child category element
                            /remove_element <name> - Remove category element
                            /help - Show available commands and their descriptions""";

                    silent.send(helpMessage, ctx.chatId());
                })
                .build();
    }

}
