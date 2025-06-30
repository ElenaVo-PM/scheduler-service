# **Установка и работа с Checkstyle**

Линтер (от англ. "linter") — это инструмент для статического анализа кода. 
Checkstyle — это линтер, который помогает поддерживать единый стиль и находить ошибки на ранних этапах.


## **Как запустить Checkstyle**

### 1. **через Gradle**

```
* ./gradlew checkstyleMain   # проверить основной код

* ./gradlew checkstyleTest   # проверить тесты

* ./gradlew check            # полная проверка
```


### 2. **в IntelliJ IDEA**

### Установите плагин Checkstyle-IDEA (через VPN):
```
File → Settings → Plugins → Marketplace → "Checkstyle-IDEA" → Install
```

### Активируйте проверку через Gradle: 
```
* scheduler-service → Tasks → other → checkstyleMain  # проверить основной код

* scheduler-service → Tasks → other → checkstyleTest  # проверить тесты
```


## **Где смотреть отчёт**
(открыть в браузере — там список ошибок с пояснениями)

``` 
build/reports/checkstyle/
├── main.html  # Для основного кода
└── test.html  # Для тестов
```



## **Настройка проверок**

Файл checkstyle.xml можно менять по договоренностям в команде. Предлагаю добавить на рассмотрение такие проверки:

#### **Импорты**
* Запрещает импорт через * (import java.util.*)
``` 
<module name="AvoidStarImport"/> 
```

#### **Форматирование**
* Требует {} даже для однострочных блоков
``` 
<module name="NeedBraces"/>
``` 

#### **Документация**
* Проверяет Javadoc для классов
``` 
<module name="JavadocType"/>
``` 

* Проверяет Javadoc для методов
``` 
<module name="JavadocMethod"/>
``` 
