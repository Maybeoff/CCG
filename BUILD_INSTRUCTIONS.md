# Инструкции по сборке Custom Click GUI Mod

## Требования

- Java 17 или выше
- Gradle 8.0 или выше
- Minecraft 1.21.1
- Fabric Loader 0.16.14
- Fabric API 0.116.4+1.21.1

## Установка Gradle

### Windows
1. Скачайте Gradle с официального сайта: https://gradle.org/releases/
2. Распакуйте архив в папку (например, `C:\gradle`)
3. Добавьте `C:\gradle\bin` в переменную PATH
4. Перезапустите командную строку

### Альтернативный способ (через Chocolatey)
```cmd
choco install gradle
```

### Альтернативный способ (через Scoop)
```cmd
scoop install gradle
```

## Сборка мода

1. Убедитесь, что Gradle установлен:
   ```cmd
   gradle --version
   ```

2. Соберите мод:
   ```cmd
   gradle build
   ```

3. Готовый JAR файл будет находиться в `build/libs/`

## Тестирование

1. Установите Fabric Loader для Minecraft 1.21.1
2. Установите Fabric API
3. Поместите JAR файл мода в папку `mods`
4. Запустите игру

## Использование

- **Правый Shift + Insert** - открыть GUI
- Используйте команды `/CCG` для управления категориями и кнопками

## Примеры команд

```cmd
/CCG add categories "тест"
/CCG add button "home" "тест" [/home home;localsay "вы телепортированы"]
/CCG rem categories "тест"
/CCG rem button "home" "тест"
```

## Устранение проблем

### Ошибка "Gradle not found"
Установите Gradle согласно инструкциям выше.

### Ошибки компиляции
Убедитесь, что у вас установлена Java 17 или выше:
```cmd
java --version
```

### Проблемы с зависимостями
Проверьте, что в `gradle.properties` указаны правильные версии:
- minecraft_version=1.21.1
- fabric_version=0.116.4+1.21.1
- loader_version=0.16.14 