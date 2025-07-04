package ru.mby.data;

import java.util.HashSet;
import java.util.Set;

public class CategoryManager {
    private static final Set<String> categories = new HashSet<>();
    
    public static boolean addCategory(String name) {
        if (categories.contains(name)) {
            return false;
        }
        categories.add(name);
        if (!ru.mby.data.ConfigManager.isLoadingProfile) ConfigManager.saveConfig(); // Автоматически сохраняем
        return true;
    }
    
    public static boolean removeCategory(String name) {
        if (!categories.contains(name)) {
            return false;
        }
        categories.remove(name);
        // Удаляем все кнопки из этой категории
        ButtonManager.removeCategoryButtons(name);
        if (!ru.mby.data.ConfigManager.isLoadingProfile) ConfigManager.saveConfig(); // Автоматически сохраняем
        return true;
    }
    
    public static Set<String> getCategories() {
        return new HashSet<>(categories);
    }
    
    public static boolean hasCategory(String name) {
        return categories.contains(name);
    }
    
    public static void clear() {
        categories.clear();
    }
} 