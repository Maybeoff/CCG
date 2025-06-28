package ru.mby.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import ru.mby.data.ButtonManager;
import ru.mby.data.CategoryManager;

import java.util.ArrayList;
import java.util.List;

public class CustomClickGuiScreen extends Screen {
    
    private List<CategoryPanel> categoryPanels;
    private int scrollOffset = 0;
    private boolean isDragging = false;
    private int lastMouseY = 0;
    
    public CustomClickGuiScreen() {
        super(Text.literal("Custom Click GUI"));
        this.categoryPanels = new ArrayList<>();
        initializePanels();
    }
    
    private void initializePanels() {
        categoryPanels.clear();
        int x = 10;
        int y = 30; // Отступ для заголовка
        
        for (String categoryName : CategoryManager.getCategories()) {
            CategoryPanel panel = new CategoryPanel(x, y, 150, 200, categoryName);
            categoryPanels.add(panel);
            x += 160; // Отступ между панелями
            
            // Если панели не помещаются в строку, переходим на новую
            if (x > width - 160) {
                x = 10;
                y += 210;
            }
        }
    }
    
    @Override
    protected void init() {
        super.init();
        initializePanels();
        
        // Добавляем кнопку закрытия
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Закрыть"), button -> {
            this.close();
        }).dimensions(width - 60, 10, 50, 20).build());
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Сначала рисуем фон (под панелями)
        context.fill(0, 0, this.width, this.height, 0xCC000000); // полупрозрачный чёрный
        // Затем рендерим заголовок и панели
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 10, 0xFFFFFF);
        for (CategoryPanel panel : categoryPanels) {
            panel.render(context, mouseX, mouseY, delta);
        }
        super.render(context, mouseX, mouseY, delta);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Проверяем клики по панелям
        for (CategoryPanel panel : categoryPanels) {
            if (panel.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }
        
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (button == 0) { // Левая кнопка мыши
            for (CategoryPanel panel : categoryPanels) {
                if (panel.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
                    return true;
                }
            }
        }
        
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }
    
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (CategoryPanel panel : categoryPanels) {
            panel.mouseReleased(mouseX, mouseY, button);
        }
        
        return super.mouseReleased(mouseX, mouseY, button);
    }
    
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        // Прокрутка для перемещения панелей
        for (CategoryPanel panel : categoryPanels) {
            panel.setY((int) (panel.getY() - amount * 10));
        }
        
        return super.mouseScrolled(mouseX, mouseY, 0, amount);
    }
    
    @Override
    public boolean shouldPause() {
        return false;
    }
} 