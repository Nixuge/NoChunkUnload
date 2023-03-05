package me.nixuge.nochunkunload.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class InitMixin {
    @Inject(method="run", at = @At("HEAD"))
    public void tkt(CallbackInfo ci) {
        System.out.println("Mixin loaded");
    }
}
