package ru.mby.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ButtonManager {
    private static final Map<String, Map<String, String>> buttons = new HashMap<>(); // category -> (buttonName -> commands)
    
    public static boolean addButton(String name, String category, String commands) {
        if (!CategoryManager.hasCategory(category)) {
            return false;
        }
        
        buttons.computeIfAbsent(category, k -> new HashMap<>()).put(name, commands);
        return true;
    }
    
    public static boolean removeButton(String name, String category) {
        Map<String, String> categoryButtons = buttons.get(category);
        if (categoryButtons == null || !categoryButtons.containsKey(name)) {
            return false;
        }
        categoryButtons.remove(name);
        return true;
    }
    
    public static void removeCategoryButtons(String category) {
        buttons.remove(category);
    }
    
    public static Map<String, String> getCategoryButtons(String category) {
        return buttons.getOrDefault(category, new HashMap<>());
    }
    
    public static Set<String> getCategories() {
        return buttons.keySet();
    }
    
    public static String getButtonCommands(String name, String category) {
        Map<String, String> categoryButtons = buttons.get(category);
        if (categoryButtons == null) {
            return null;
        }
        return categoryButtons.get(name);
    }
} 