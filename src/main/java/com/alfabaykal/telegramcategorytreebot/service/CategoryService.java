package com.alfabaykal.telegramcategorytreebot.service;

import com.alfabaykal.telegramcategorytreebot.model.Category;

import java.util.Set;

/**
 * Сервис для работы с категориями
 */
public interface CategoryService {
    /**
     * Возвращает текстовое представление дерева категорий
     *
     * @return Текстовое представление дерева категорий
     */
    String viewTree();
    /**
     * Добавляет новую категорию с указанным именем
     *
     * @param elementName Имя новой категории
     * @return Добавленная категория
     */
    Category addElement(String elementName);
    /**
     * Добавляет новую подкатегорию с указанным именем к существующей родительской категории
     *
     * @param parentName Имя родительской категории
     * @param childName  Имя новой подкатегории
     * @return Добавленная подкатегория
     */
    Category addChildElement(String parentName, String childName);
    /**
     * Удаляет категорию с указанным именем
     *
     * @param elementName Имя категории для удаления
     */
    void removeElement(String elementName);
}
