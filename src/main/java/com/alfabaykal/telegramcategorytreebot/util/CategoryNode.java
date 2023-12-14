package com.alfabaykal.telegramcategorytreebot.util;

import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.text.ConsoleText;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий узел дерева категорий
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryNode implements TreeNode {
    /** Имя узла */
    private String name;
    /** Родительский узел */
    private CategoryNode parent;
    /** Список дочерних узлов */
    private List<CategoryNode> children;

    /**
     * Получает содержимое узла в виде текста для отображения
     *
     * @return Объект ConsoleText, представляющий содержимое узла
     */
    @Override
    public ConsoleText content() {
        return ConsoleText.of(name);
    }

    /**
     * Получает список дочерних узлов
     *
     * @return Список дочерних узлов в виде объектов TreeNode
     */
    @Override
    public List<TreeNode> children() {
        List<TreeNode> categoryNode = new ArrayList<>();
        for (CategoryNode category : children) {
            categoryNode.add(new CategoryNode(category.name, category.parent, category.children));
        }
        return categoryNode;
    }
}