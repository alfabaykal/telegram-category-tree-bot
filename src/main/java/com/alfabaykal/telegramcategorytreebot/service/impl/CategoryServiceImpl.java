package com.alfabaykal.telegramcategorytreebot.service.impl;

import com.alfabaykal.telegramcategorytreebot.exception.CategoryAlreadyExistException;
import com.alfabaykal.telegramcategorytreebot.exception.CategoryNotFoundException;
import com.alfabaykal.telegramcategorytreebot.model.Category;
import com.alfabaykal.telegramcategorytreebot.repository.CategoryRepository;
import com.alfabaykal.telegramcategorytreebot.service.CategoryService;
import com.alfabaykal.telegramcategorytreebot.util.EntityNodeConverter;
import hu.webarticum.treeprinter.printer.listing.ListingTreePrinter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Реализация интерфейса {@link com.alfabaykal.telegramcategorytreebot.service.CategoryService}
 */
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EntityNodeConverter converter;

    /**
     * Конструктор для создания объекта сервиса
     *
     * @param categoryRepository Репозиторий для взаимодействия с базой данных
     * @param converter          Конвертер для преобразования сущностей в узлы дерева
     */
    public CategoryServiceImpl(CategoryRepository categoryRepository, EntityNodeConverter converter) {
        this.categoryRepository = categoryRepository;
        this.converter = converter;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public String viewTree() {
        if (categoryRepository.findAll().isEmpty())
            return "There's no any category";
        StringBuilder stringBuilder = new StringBuilder();
        List<Category> rootCategories = getRootCategories();
        for (Category category : rootCategories) {
            stringBuilder.append(new ListingTreePrinter()
                    .stringify(converter.convertCategoryToCategoryNode(categoryRepository.findByName(category.getName())
                            .orElseThrow(() -> new CategoryNotFoundException(category.getName())))));
        }
        return stringBuilder.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Category addElement(String elementName) {
        if (elementName.isEmpty())
            throw new IllegalArgumentException("Category names doesn't be empty");
        if (categoryRepository.findByName(elementName).isPresent())
            throw new CategoryAlreadyExistException(elementName);

        log.info("Adding new root category: " + elementName);
        return categoryRepository.save(new Category(elementName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Category addChildElement(String parentName, String childName) {
        if (categoryRepository.findByName(childName).isPresent())
            throw new CategoryAlreadyExistException(childName);
        if (childName.isEmpty() || parentName.isEmpty())
            throw new IllegalArgumentException("Category names doesn't be empty");

        Category category = new Category(childName);
        category.setParent(categoryRepository.findByName(parentName)
                .orElseThrow(() -> new CategoryNotFoundException(parentName)));
        log.info("Adding new subcategory " + childName + " to category " + parentName);
        return categoryRepository.save(category);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void removeElement(String elementName) {
        if (categoryRepository.findByName(elementName).isEmpty())
            throw new CategoryNotFoundException(elementName);
        log.info("Remove category: " + elementName);
        categoryRepository.deleteCategoryByName(elementName);
    }

    private List<Category> getRootCategories() {
        List<Category> rootCategories = new ArrayList<>();
        for (Category category : categoryRepository.findAll()) {
            if (category.getParent() == null)
                rootCategories.add(category);
        }
        return rootCategories;
    }
}
