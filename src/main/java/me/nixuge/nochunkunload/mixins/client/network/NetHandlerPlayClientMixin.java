package me.nixuge.nochunkunload.mixins.client.network;

import me.nixuge.nochunkunload.McMod;
import me.nixuge.nochunkunload.config.Cache;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class NetHandlerPlayClientMixin {
    private final Cache cache = McMod.getInstance().getCache();

    @Inject(method = "handleBlockChange", at = @At("HEAD"), cancellable = true)
    public void blockChange(S23PacketBlockChange p_handleBlockChange_1_, CallbackInfo ci) {
        if (cache.isWorldFrozen()) {
            ci.cancel();
        }
    }

    // Unneeded as this just wraps around handleBlockChange()
    // but might as well have it avoid running unnecessary calculations
    @Inject(method = "handleMultiBlockChange", at = @At("HEAD"), cancellable = true)
    public void multiBlockChange(S22PacketMultiBlockChange p_handleMultiBlockChange_1_, CallbackInfo ci) {
        if (cache.isWorldFrozen()) {
            ci.cancel();
        }
    }

    // Never saw that used but might as well
    @Inject(method = "handleExplosion", at = @At("HEAD"), cancellable = true)
    public void explosion(S27PacketExplosion p_handleExplosion_1_, CallbackInfo ci) {
        if (cache.isWorldFrozen()) {
            ci.cancel();
        }
    }

    // save already loaded chunks
    @Inject(method = "handleChunkData", at = @At("HEAD"), cancellable = true)
    public void chunkData(S21PacketChunkData p_handleChunkData_1_, CallbackInfo ci) {
        if (this.cache.isSavedChunk(p_handleChunkData_1_.getChunkX(), p_handleChunkData_1_.getChunkZ())) {
            if (cache.isWorldFrozen()) {
                ci.cancel();
            }
        }
    }
    @Inject(method = "handleChunkData", at = @At("RETURN"))
    public void chunkDataEnd(S21PacketChunkData p_handleChunkData_1_, CallbackInfo ci) {
        this.cache.addSavedChunk(p_handleChunkData_1_.getChunkX(), p_handleChunkData_1_.getChunkZ());
    }

    @Inject(method = "handleUpdateTileEntity", at = @At("RETURN"), cancellable = true)
    public void updateTileEntity(S35PacketUpdateTileEntity p_handleUpdateTileEntity_1_, CallbackInfo ci) {
        if (cache.isWorldFrozen()) {
            ci.cancel();
        }
    }

    @Inject(method = "handleBlockAction", at = @At("RETURN"), cancellable = true)
    public void blockAction(S24PacketBlockAction p_handleBlockAction_1_, CallbackInfo ci) {
        if (cache.isWorldFrozen()) {
            ci.cancel();
        }
    }

    @Inject(method = "handleBlockBreakAnim", at = @At("RETURN"), cancellable = true)
    public void blockAction(S25PacketBlockBreakAnim p_handleBlockBreakAnim_1_, CallbackInfo ci) {
        if (cache.isWorldFrozen()) {
            ci.cancel();
        }
    }
}
