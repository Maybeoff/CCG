package ru.mby.mixins;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class NoBlurMixin {
    @Inject(
        method = "renderBackground(Lnet/minecraft/client/gui/DrawContext;IIF)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void disableBlur(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        // Отменяем вызов renderBackground, чтобы убрать блюр
        ci.cancel();
    }
} 