package com.alfabaykal.telegramcategorytreebot.repository;

import com.alfabaykal.telegramcategorytreebot.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String categoryName);

    void deleteCategoryByName(String categoryName);
}
