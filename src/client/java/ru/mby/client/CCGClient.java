package ru.mby.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import ru.mby.gui.CustomClickGuiScreen;

public class CCGClient implements ClientModInitializer {
    
    private static KeyBinding openGuiKey;

    @Override
    public void onInitializeClient() {
        // Регистрируем клавишу для открытия GUI (правый Shift + Insert)
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.ccg.open_gui",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_INSERT,
            "category.ccg.general"
        ));

        // Обработчик нажатия клавиши
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (openGuiKey.wasPressed()) {
                // Проверяем, что правый Shift нажат
                if (InputUtil.isKeyPressed(client.getWindow().getHandle(), GLFW.GLFW_KEY_RIGHT_SHIFT)) {
                    client.setScreen(new CustomClickGuiScreen());
                }
            }
        });
    }
}