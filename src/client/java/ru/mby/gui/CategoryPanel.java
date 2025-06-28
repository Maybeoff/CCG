package ru.mby.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import ru.mby.commands.CommandExecutor;
import ru.mby.data.ButtonManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoryPanel {
    private int x, y, width, height;
    private String categoryName;
    private List<ButtonWidget> buttons;
    private boolean isDragging = false;
    private int dragOffsetX, dragOffsetY;
    
    public CategoryPanel(int x, int y, int width, int height, String categoryName) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.categoryName = categoryName;
        this.buttons = new ArrayList<>();
        initializeButtons();
    }
    
    private void initializeButtons() {
        buttons.clear();
        Map<String, String> categoryButtons = ButtonManager.getCategoryButtons(categoryName);
        
        int buttonY = y + 25; // Отступ от заголовка
        int buttonHeight = 20;
        int buttonSpacing = 5;
        
        for (Map.Entry<String, String> entry : categoryButtons.entrySet()) {
            String buttonName = entry.getKey();
            String commands = entry.getValue();
            
            ButtonWidget button = ButtonWidget.builder(Text.literal(buttonName), btn -> {
                CommandExecutor.executeCommands(commands);
            }).dimensions(x + 5, buttonY, width - 10, buttonHeight).build();
            
            buttons.add(button);
            buttonY += buttonHeight + buttonSpacing;
        }
    }
    
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient client = MinecraftClient.getInstance();
        
        // Рендерим фон панели
        context.fill(x, y, x + width, y + height, 0x88000000);
        
        // Рендерим заголовок
        context.fill(x, y, x + width, y + 20, 0x88FF6B6B);
        context.drawTextWithShadow(client.textRenderer, categoryName, x + 5, y + 6, 0xFFFFFF);
        
        // Рендерим кнопки
        for (ButtonWidget button : buttons) {
            button.render(context, mouseX, mouseY, delta);
        }
        
        // Рендерим границу
        drawBorder(context, x, y, width, height, 0xFFFFFFFF);
    }
    
    private void drawBorder(DrawContext context, int x, int y, int width, int height, int color) {
        context.fill(x, y, x + width, y + 1, color); // Верхняя граница
        context.fill(x, y + height - 1, x + width, y + height, color); // Нижняя граница
        context.fill(x, y, x + 1, y + height, color); // Левая граница
        context.fill(x + width - 1, y, x + width, y + height, color); // Правая граница
    }
    
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Проверяем клик по заголовку для перетаскивания
        if (button == 0 && mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + 20) {
            isDragging = true;
            dragOffsetX = (int) (mouseX - x);
            dragOffsetY = (int) (mouseY - y);
            return true;
        }
        
        // Проверяем клики по кнопкам
        for (ButtonWidget buttonWidget : buttons) {
            if (buttonWidget.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }
        
        return false;
    }
    
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (isDragging && button == 0) {
            x = (int) (mouseX - dragOffsetX);
            y = (int) (mouseY - dragOffsetY);
            
            // Обновляем позиции кнопок
            updateButtonPositions();
            return true;
        }
        
        return false;
    }
    
    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            isDragging = false;
        }
    }
    
    private void updateButtonPositions() {
        int buttonY = y + 25;
        int buttonHeight = 20;
        int buttonSpacing = 5;
        
        for (ButtonWidget button : buttons) {
            button.setPosition(x + 5, buttonY);
            buttonY += buttonHeight + buttonSpacing;
        }
    }
    
    public void setY(int y) {
        this.y = y;
        updateButtonPositions();
    }
    
    public int getY() {
        return y;
    }
} 