package ru.mby.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_FILE = "ccg_config.json";
    private static final String DEFAULT_PROFILE_FILE = "ccg_default.txt";
    private static File configFile;
    public static boolean isLoadingProfile = false;
    
    public static void init() {
        Path configDir = FabricLoader.getInstance().getConfigDir();
        configFile = configDir.resolve(CONFIG_FILE).toFile();
        // Автозагрузка профиля, если задан
        String defaultProfile = getDefaultProfile();
        if (defaultProfile != null && !defaultProfile.isEmpty()) {
            loadProfile(defaultProfile);
        } else {
            loadConfig();
        }
    }
    
    public static void saveConfig() {
        saveProfile(null);
    }
    
    public static void loadConfig() {
        loadProfile(null);
    }
    
    public static void saveProfile(String profileName) {
        try {
            Path configDir = FabricLoader.getInstance().getConfigDir();
            File file = (profileName == null || profileName.isEmpty()) ? configFile : configDir.resolve("ccg_" + profileName + ".json").toFile();
            JsonObject config = new JsonObject();
            // Сохраняем категории
            JsonObject categoriesJson = new JsonObject();
            for (String category : CategoryManager.getCategories()) {
                categoriesJson.addProperty(category, "true");
            }
            config.add("categories", categoriesJson);
            // Сохраняем кнопки
            JsonObject buttonsJson = new JsonObject();
            for (String category : ButtonManager.getCategories()) {
                JsonObject categoryButtons = new JsonObject();
                Map<String, String> buttons = ButtonManager.getCategoryButtons(category);
                for (Map.Entry<String, String> entry : buttons.entrySet()) {
                    categoryButtons.addProperty(entry.getKey(), entry.getValue());
                }
                buttonsJson.add(category, categoryButtons);
            }
            config.add("buttons", buttonsJson);
            // Записываем в файл
            try (FileWriter writer = new FileWriter(file)) {
                GSON.toJson(config, writer);
            }
            System.out.println("[CCG] Профиль сохранён: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении профиля CCG: " + e.getMessage());
        }
    }
    
    public static void loadProfile(String profileName) {
        isLoadingProfile = true;
        try {
            Path configDir = FabricLoader.getInstance().getConfigDir();
            File file = (profileName == null || profileName.isEmpty()) ? configFile : configDir.resolve("ccg_" + profileName + ".json").toFile();
            if (!file.exists()) {
                System.out.println("[CCG] Профиль не найден: " + file.getAbsolutePath());
                return;
            }
            try (FileReader reader = new FileReader(file)) {
                JsonObject config = JsonParser.parseReader(reader).getAsJsonObject();
                // Очищаем старые данные
                CategoryManager.clear();
                ButtonManager.clear();
                // Загружаем категории
                if (config.has("categories")) {
                    JsonObject categoriesJson = config.getAsJsonObject("categories");
                    for (String category : categoriesJson.keySet()) {
                        CategoryManager.addCategory(category);
                    }
                }
                // Загружаем кнопки
                if (config.has("buttons")) {
                    JsonObject buttonsJson = config.getAsJsonObject("buttons");
                    for (String category : buttonsJson.keySet()) {
                        JsonObject categoryButtons = buttonsJson.getAsJsonObject(category);
                        for (String buttonName : categoryButtons.keySet()) {
                            String commands = categoryButtons.get(buttonName).getAsString();
                            ButtonManager.addButton(buttonName, category, commands);
                        }
                    }
                }
                System.out.println("[CCG] Профиль загружен: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке профиля CCG: " + e.getMessage());
        } finally {
            isLoadingProfile = false;
        }
    }
    
    public static void setDefaultProfile(String profileName) {
        try {
            Path configDir = FabricLoader.getInstance().getConfigDir();
            File file = configDir.resolve(DEFAULT_PROFILE_FILE).toFile();
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(profileName == null ? "" : profileName);
            }
            System.out.println("[CCG] Профиль по умолчанию: " + profileName);
        } catch (IOException e) {
            System.err.println("Ошибка при установке профиля по умолчанию: " + e.getMessage());
        }
    }
    
    public static String getDefaultProfile() {
        try {
            Path configDir = FabricLoader.getInstance().getConfigDir();
            File file = configDir.resolve(DEFAULT_PROFILE_FILE).toFile();
            if (!file.exists()) return null;
            try (FileReader reader = new FileReader(file)) {
                char[] buf = new char[64];
                int len = reader.read(buf);
                if (len > 0) {
                    return new String(buf, 0, len).trim();
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении профиля по умолчанию: " + e.getMessage());
        }
        return null;
    }
} 