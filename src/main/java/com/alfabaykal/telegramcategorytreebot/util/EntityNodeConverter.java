package com.alfabaykal.telegramcategorytreebot.util;

import com.alfabaykal.telegramcategorytreebot.model.Category;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Класс для конвертации между объектами Category и CategoryNode
 */
@Component
public class EntityNodeConverter {

    /**
     * Объект ModelMapper для выполнения конвертации
     */
    private final ModelMapper modelMapper;

    /**
     * Конструктор класса EntityNodeConverter
     *
     * @param modelMapper Объект ModelMapper, предоставляющий функциональность конвертации
     */
    public EntityNodeConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        // Создание типовых отображений для конвертации между Category и CategoryNode
        modelMapper.createTypeMap(Category.class, CategoryNode.class);
        modelMapper.createTypeMap(CategoryNode.class, Category.class);
    }

    /**
     * Конвертирует объект Category в объект CategoryNode
     *
     * @param category Объект Category, который необходимо сконвертировать
     * @return Сконвертированный объект CategoryNode
     */
    public CategoryNode convertCategoryToCategoryNode(Category category) {
        return modelMapper.map(category, CategoryNode.class);
    }

}
