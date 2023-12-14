# Telegram Category Tree Bot

Telegram Category Tree Bot - это бот для управления деревом категорий с использованием Spring Boot и PostgreSQL

## Описание

Этот бот разработан для удобного управления и визуализации иерархии категорий в мессенджере Telegram.

## Возможности

- **Просмотр дерева категорий:** Получайте полное представление о структуре категорий.

- **Добавление элементов:** Легко добавляйте новые категории в дерево.

- **Добавление дочерних элементов:** Расширяйте дерево, добавляя дочерние категории.

- **Удаление элементов:** Удаляйте категории, не нарушая целостность структуры.

## Использование

1. **Команда /start:** Начало общения с ботом, приветственное сообщение.

2. **Команда /view_tree:** Просмотреть все элементы дерева категорий.

3. **Команда /add_element <название>:** Добавить новый элемент в дерево.

4. **Команда /add_child_element <родитель> <дочерний_элемент>:** Добавить дочерний элемент.

5. **Команда /remove_element <название>:** Удалить элемент из дерева.

6. **Команда /help:** Отображение списка доступных команд и их описания.

## Требования

- Docker

## Настройка
1. Перейдите в директорию проекта.
2. Пользуясь шаблоном `application.properties.origin` создайте файл `application.properties` и укажите необходимые настройки.
3. Пользуясь шаблоном `.env.origin` создайте файл `.env` и опишите необходимые настройки.
4. Соберите образ при помощи команды `docker build <имя_вашего_образа> .`

Или можете связаться со мной и я предоставлю все необходимые данные для тестирования приложения.
Образ вы сможете найти [здесь](https://hub.docker.com/repository/docker/alfabaykal/telegram-tree-bot/general).

## Запуск Docker-compose:
В корневой директории проекта выполните команду:
`docker-compose up -d`, это создаст и запустит контейнеры для приложения и базы данных.

## Использование Telegram Bot:
Откройте Telegram и найдите вашего бота, который был создан с помощью данного проекта. Теперь вы можете взаимодействовать с ботом, используя команды, такие как /start, /view_tree, /add_element, /add_child_element, /remove_element.

## Остановка и удаление контейнеров:
Чтобы остановить и удалить контейнеры, выполните: `docker-compose down`
Это завершит работу приложения и удалит созданные контейнеры.

## Пример реализации дерева категорий через бот
Результат работы команды `/view_tree`
```
Cars
 ├─Audi
 │  ├─A1
 │  ├─A2
 │  ├─A3
 │  ├─A5
 │  └─A4
 ├─BMW
 ├─Mercedes-Benz
 ├─Toyota
 ├─Subaru
 └─Dodge
Airplanes
 ├─Boeing
 │  ├─747
 │  ├─777
 │  └─737
 └─Airbus
```