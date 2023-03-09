package me.nixuge.nochunkunload.mixins.client.network;

import me.nixuge.nochunkunload.McMod;
import me.nixuge.nochunkunload.config.Cache;
import me.nixuge.nochunkunload.mixins.server.S26PacketMapChunkBulkGetters;
import me.nixuge.nochunkunload.mixins.server.S26PacketMapChunkBulkSetters;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

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
        // If is a saved chunk
        if (this.cache.isSavedChunk(p_handleChunkData_1_.getChunkX(), p_handleChunkData_1_.getChunkZ())) {
            if (cache.isWorldFrozen()) {
                ci.cancel();
            }
            return;
        }
        this.cache.addSavedChunk(p_handleChunkData_1_.getChunkX(), p_handleChunkData_1_.getChunkZ());
    }

    /*
     * Due to a problem in Mixin 0.7.11 (or pre-0.8 really), you can't call a function
     * in a Mixin class from another Mixin class.
     * This basically prevents me from having this work and is pretty much not doable rn.
     *
     * POSSIBLE SOLUTIONS:
     * - Get 0.8 Mixins working on 1.8.9 (would be the best)
     *
     * - Inject directly onto S26PacketMapChunkBulk on both the constructor(List<Chunk>) and
     * - the readPacketData(PacketBuffer) methods (either Inject or ModifyArg or ModifyVariable or smth)
     * -- But I couldn't inject into the constructor, so won't right now.
     * -- (+ I'm pretty sure having no chunks in one of those packets would cause issues anyways)
     *
     * - Go the suboptimal way and ONLY process S26PacketMapChunkBulk packets if none of the chunks
     * - inside it are already loaded (could cause some chunks to not load)
     *
     * For now, this code is staying here unused.
     * It can be reactivated at any time by just uncommenting the @ModifyVariable line on top of it.
     */

    // @ModifyVariable(method = "handleMapChunkBulk", at = @At("HEAD"))
    public S26PacketMapChunkBulk mapChunkBulk(S26PacketMapChunkBulk p_handleMapChunkBulk_1_) {
        // Make a list to store valid chunk indexes
        List<Integer> validIndexes = new ArrayList<>();
        for (int i = 0; i < p_handleMapChunkBulk_1_.getChunkCount(); ++i) {
            if (!this.cache.isSavedChunk(p_handleMapChunkBulk_1_.getChunkX(i), p_handleMapChunkBulk_1_.getChunkZ(i))) {
                validIndexes.add(i);
            }
        }

        int newSize = validIndexes.size();
        if (newSize == 0)
            return new S26PacketMapChunkBulk();

        // Get data for current packet
        boolean isOverworld = ((S26PacketMapChunkBulkGetters) p_handleMapChunkBulk_1_).isOverworld();
        S21PacketChunkData.Extracted[] oldChunkData = ((S26PacketMapChunkBulkGetters) p_handleMapChunkBulk_1_)
                .getChunksData();
        int[] oldxPositions = ((S26PacketMapChunkBulkGetters) p_handleMapChunkBulk_1_).getxPositions();
        int[] oldzPositions = ((S26PacketMapChunkBulkGetters) p_handleMapChunkBulk_1_).getzPositions();

        // Make empty data for the new packet
        S21PacketChunkData.Extracted[] chunkData = new S21PacketChunkData.Extracted[newSize];
        int[] xPositions = new int[newSize];
        int[] zPositions = new int[newSize];

        int newIndex = 0;
        for (int index : validIndexes) {
            chunkData[newIndex] = oldChunkData[index];
            xPositions[newIndex] = oldxPositions[index];
            zPositions[newIndex] = oldzPositions[index];
            newIndex++;
        }

        // instantiate the new packet and give it its needed values
        S26PacketMapChunkBulk newPacket = new S26PacketMapChunkBulk();

        S26PacketMapChunkBulkSetters setter = ((S26PacketMapChunkBulkSetters) newPacket);
        setter.setChunks(chunkData);
        setter.setxPositions(xPositions);
        setter.setzPositions(zPositions);
        setter.setIsOverworld(isOverworld);

        return newPacket;
    }
}
