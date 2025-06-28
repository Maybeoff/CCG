package ru.mby;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import ru.mby.commands.CCGCommand;

public class CCG implements ModInitializer {

    @Override
    public void onInitialize() {
        // Регистрируем команду CCG
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            CCGCommand.register(dispatcher);
        });
    }
}