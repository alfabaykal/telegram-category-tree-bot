package com.alfabaykal.telegramcategorytreebot.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Класс, представляющий категорию в иерархии категорий
 */
@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "category")
public class Category {
    /**
     * Уникальный идентификатор категории
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Наименование категории
     */
    @NonNull
    @Column(name = "name")
    private String name;
    /**
     * Родительская категория, к которой принадлежит данная категория
     */
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;
    /**
     * Список подкатегорий, входящих в данную категорию
     */
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> children;

}
