package ru.mby.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import ru.mby.data.CategoryManager;
import ru.mby.data.ButtonManager;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CCGCommand {
    
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("CCG")
            .then(literal("add")
                .then(literal("categories")
                    .then(argument("name", StringArgumentType.string())
                        .executes(CCGCommand::addCategory)))
                .then(literal("button")
                    .then(argument("name", StringArgumentType.string())
                        .then(argument("category", StringArgumentType.string())
                            .then(argument("commands", StringArgumentType.greedyString())
                                .executes(CCGCommand::addButton))))))
            .then(literal("rem")
                .then(literal("categories")
                    .then(argument("name", StringArgumentType.string())
                        .executes(CCGCommand::removeCategory)))
                .then(literal("button")
                    .then(argument("name", StringArgumentType.string())
                        .then(argument("category", StringArgumentType.string())
                            .executes(CCGCommand::removeButton)))))
            .then(literal("cfg")
                .then(literal("save")
                    .then(argument("name", StringArgumentType.string())
                        .executes(ctx -> {
                            String name = StringArgumentType.getString(ctx, "name");
                            ru.mby.data.ConfigManager.saveProfile(name);
                            ctx.getSource().sendMessage(Text.literal("§aПрофиль '" + name + "' сохранён!"));
                            return 1;
                        })
                    )
                )
                .then(literal("load")
                    .then(argument("name", StringArgumentType.string())
                        .executes(ctx -> {
                            String name = StringArgumentType.getString(ctx, "name");
                            ru.mby.data.ConfigManager.loadProfile(name);
                            ctx.getSource().sendMessage(Text.literal("§aПрофиль '" + name + "' загружен!"));
                            return 1;
                        })
                    )
                )
                .then(literal("default")
                    .then(argument("name", StringArgumentType.string())
                        .executes(ctx -> {
                            String name = StringArgumentType.getString(ctx, "name");
                            ru.mby.data.ConfigManager.setDefaultProfile(name);
                            ctx.getSource().sendMessage(Text.literal("§aПрофиль '" + name + "' теперь загружается по умолчанию!"));
                            return 1;
                        })
                    )
                )
            )
        );
    }
    
    private static int addCategory(CommandContext<ServerCommandSource> context) {
        String name = StringArgumentType.getString(context, "name");
        try {
            boolean success = CategoryManager.addCategory(name);
            if (success) {
                context.getSource().sendMessage(Text.literal("§aКатегория '" + name + "' успешно добавлена!"));
            } else {
                context.getSource().sendMessage(Text.literal("§eКатегория '" + name + "' уже существует!"));
            }
        } catch (Exception e) {
            context.getSource().sendMessage(Text.literal("§cПроизошла ошибка при добавлении категории!"));
            System.err.println("[CCG] Ошибка при добавлении категории: " + e.getMessage());
            e.printStackTrace();
        }
        return 1;
    }
    
    private static int removeCategory(CommandContext<ServerCommandSource> context) {
        String name = StringArgumentType.getString(context, "name");
        boolean success = CategoryManager.removeCategory(name);
        
        if (success) {
            context.getSource().sendMessage(Text.literal("§aКатегория '" + name + "' успешно удалена!"));
        } else {
            context.getSource().sendMessage(Text.literal("§cКатегория '" + name + "' не найдена!"));
        }
        
        return 1;
    }
    
    private static int addButton(CommandContext<ServerCommandSource> context) {
        String name = StringArgumentType.getString(context, "name");
        String category = StringArgumentType.getString(context, "category");
        String commands = StringArgumentType.getString(context, "commands");
        try {
            boolean success = ButtonManager.addButton(name, category, commands);
            if (success) {
                context.getSource().sendMessage(Text.literal("§aКнопка '" + name + "' успешно добавлена в категорию '" + category + "'!"));
            } else {
                context.getSource().sendMessage(Text.literal("§eОшибка при добавлении кнопки. Проверьте, что категория существует и кнопка не дублируется."));
            }
        } catch (Exception e) {
            context.getSource().sendMessage(Text.literal("§cПроизошла ошибка при добавлении кнопки!"));
            System.err.println("[CCG] Ошибка при добавлении кнопки: " + e.getMessage());
            e.printStackTrace();
        }
        return 1;
    }
    
    private static int removeButton(CommandContext<ServerCommandSource> context) {
        String name = StringArgumentType.getString(context, "name");
        String category = StringArgumentType.getString(context, "category");
        try {
            boolean success = ButtonManager.removeButton(name, category);
            if (success) {
                context.getSource().sendMessage(Text.literal("§aКнопка '" + name + "' успешно удалена из категории '" + category + "'!"));
            } else {
                context.getSource().sendMessage(Text.literal("§eКнопка '" + name + "' не найдена в категории '" + category + "'!"));
            }
        } catch (Exception e) {
            context.getSource().sendMessage(Text.literal("§cПроизошла ошибка при удалении кнопки!"));
            System.err.println("[CCG] Ошибка при удалении кнопки: " + e.getMessage());
            e.printStackTrace();
        }
        return 1;
    }
} 