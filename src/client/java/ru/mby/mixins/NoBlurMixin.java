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
        Screen screen = (Screen) (Object) this;
        if (screen instanceof ru.mby.gui.CustomClickGuiScreen) {
            context.fill(0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), 0x80000000);
            ci.cancel();
        }
        // Для остальных экранов оставляем стандартное поведение
    }
} 