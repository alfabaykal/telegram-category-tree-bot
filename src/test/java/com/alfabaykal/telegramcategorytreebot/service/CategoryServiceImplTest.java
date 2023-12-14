package com.alfabaykal.telegramcategorytreebot.service;

import com.alfabaykal.telegramcategorytreebot.exception.CategoryAlreadyExistException;
import com.alfabaykal.telegramcategorytreebot.exception.CategoryNotFoundException;
import com.alfabaykal.telegramcategorytreebot.model.Category;
import com.alfabaykal.telegramcategorytreebot.repository.CategoryRepository;
import com.alfabaykal.telegramcategorytreebot.service.impl.CategoryServiceImpl;
import com.alfabaykal.telegramcategorytreebot.util.CategoryNode;
import com.alfabaykal.telegramcategorytreebot.util.EntityNodeConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private EntityNodeConverter converter;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testViewTreeNonEmpty() {
        String rootCategoryName = "RootCategory";
        Category rootCategory = new Category(rootCategoryName);
        when(categoryRepository.findAll()).thenReturn(List.of(rootCategory));
        when(categoryRepository.findByName(rootCategoryName)).thenReturn(Optional.of(rootCategory));

        when(converter.convertCategoryToCategoryNode(rootCategory)).thenReturn(new CategoryNode(rootCategoryName, null, Collections.emptyList()));

        CategoryServiceImpl categoryService = new CategoryServiceImpl(categoryRepository, converter);
        String result = categoryService.viewTree();

        assertNotNull(result);
    }

    @Test
    public void testViewTreeEmpty() {
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        String result = categoryService.viewTree();

        assertEquals("There's no any category", result);
    }

    @Test
    public void testAddElement() {
        String elementName = "TestElement";
        when(categoryRepository.findByName(elementName)).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(new Category(elementName));

        Category result = categoryService.addElement(elementName);

        verify(categoryRepository, times(1)).save(any(Category.class));
        assertEquals(elementName, result.getName());
    }

    @Test
    public void testAddElementAlreadyExists() {
        String elementName = "TestElement";
        when(categoryRepository.findByName(elementName)).thenReturn(Optional.of(new Category(elementName)));

        assertThrows(CategoryAlreadyExistException.class, () -> categoryService.addElement(elementName));
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    public void testAddElementWithNullOrEmptyName() {
        String elementName = "";

        assertThrows(IllegalArgumentException.class, () -> categoryService.addElement(elementName));
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    public void testAddChildElement() {
        String parentElement = "ParentElement";
        String childElement = "ChildElement";
        when(categoryRepository.findByName(childElement)).thenReturn(Optional.empty());
        when(categoryRepository.findByName(parentElement)).thenReturn(Optional.of(new Category(parentElement)));
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocationOnMock -> {
            Category savedCategory = invocationOnMock.getArgument(0);
            savedCategory.setId(1L);
            return savedCategory;
        });

        Category result = categoryService.addChildElement(parentElement, childElement);

        verify(categoryRepository, times(1)).save(any(Category.class));
        assertEquals(childElement, result.getName());
        assertEquals(parentElement, result.getParent().getName());
    }

    @Test
    public void testAddChildElementAlreadyExist() {
        String parentElement = "ParentElement";
        String childElement = "ChildElement";
        when(categoryRepository.findByName(childElement)).thenReturn(Optional.of(new Category(childElement)));

        assertThrows(CategoryAlreadyExistException.class, () -> categoryService.addChildElement(parentElement, childElement));
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    public void testAddChildElementWithNullOrEmptyName() {
        String parentElement = "ParentElement";
        String childElement = "";

        assertThrows(IllegalArgumentException.class, () -> categoryService.addChildElement(parentElement, childElement));
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    public void testAddChildElementToNonexistentParent() {
        String parentElement = "NonexistentParent";
        String childElement = "ChildElement";
        when(categoryRepository.findByName(parentElement)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.addChildElement(parentElement, childElement));
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    public void testRemoveElement() {
        String elementName = "ExistingCategory";
        when(categoryRepository.findByName(elementName)).thenReturn(Optional.of(new Category(elementName)));

        categoryService.removeElement(elementName);

        verify(categoryRepository, times(1)).deleteCategoryByName(elementName);
    }

    @Test
    public void testRemoveElementCategoryNotFound() {
        String elementName = "NonexistentCategory";
        when(categoryRepository.findByName(elementName)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.removeElement(elementName));
        verify(categoryRepository, never()).deleteCategoryByName(elementName);
    }

}
