package me.nixuge.nochunkunload.mixins.client.network;

import me.nixuge.nochunkunload.McMod;
import me.nixuge.nochunkunload.config.Cache;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.network.play.server.S23PacketBlockChange;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class NetHandlerPlayClientMixin {
    private final Cache cache = McMod.getInstance().getCache();

    @Inject(method = "handleBlockChange", at=@At("HEAD"), cancellable = true)
    public void change(S23PacketBlockChange p_handleBlockChange_1_, CallbackInfo ci) {
        if (cache.isWorldFrozen()) {
            ci.cancel();
        }
    }

    // unneeded as this just wraps around handleBlockChange()
    // but meh
    @Inject(method = "handleMultiBlockChange", at = @At("HEAD"), cancellable = true)
    public void multiChange(S22PacketMultiBlockChange p_handleMultiBlockChange_1_, CallbackInfo ci) {
        if (cache.isWorldFrozen()) {
            ci.cancel();
        }
    }
}
