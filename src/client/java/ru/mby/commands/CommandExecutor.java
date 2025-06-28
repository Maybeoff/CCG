package ru.mby.commands;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;

public class CommandExecutor {
    
    public static void executeCommands(String commands) {
        if (commands == null || commands.trim().isEmpty()) {
            return;
        }
        
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) {
            return;
        }
        
        // Разделяем команды по точке с запятой
        String[] commandArray = commands.split(";");
        
        for (String command : commandArray) {
            command = command.trim();
            if (command.isEmpty()) {
                continue;
            }
            
            // Проверяем, является ли это локальной командой
            if (command.startsWith("localsay ")) {
                String message = command.substring(9); // Убираем "localsay "
                player.sendMessage(Text.literal("§7[Локально] §f" + message), false);
            } else {
                // Убираем ведущий слэш, если он есть
                if (command.startsWith("/")) {
                    command = command.substring(1);
                }
                // Выполняем обычную команду
                player.networkHandler.sendCommand(command);
            }
        }
    }
} 