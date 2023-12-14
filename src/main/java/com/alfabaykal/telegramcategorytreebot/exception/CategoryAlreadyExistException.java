package com.alfabaykal.telegramcategorytreebot.exception;

public class CategoryAlreadyExistException extends RuntimeException {
    public CategoryAlreadyExistException(String categoryName) {
        super("Category with name " + categoryName + " already exist");
    }
}
