package me.nixuge.nochunkunload.mixins.client.network;

import me.nixuge.nochunkunload.McMod;
import me.nixuge.nochunkunload.config.Cache;
import me.nixuge.nochunkunload.utils.reflection.ChunkProvider;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.*;
import net.minecraft.world.chunk.Chunk;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;

import java.util.stream.IntStream;

@Mixin(NetHandlerPlayClient.class)
public class NetHandlerPlayClientMixin {
    private final Cache cache = McMod.getInstance().getCache();

    @Inject(method = "handleBlockChange", at = @At("HEAD"), cancellable = true)
    public void blockChange(SPacketBlockChange packetBlockChange, CallbackInfo ci) {
        if (cache.isWorldFrozen()) {
            ci.cancel();
        }
    }

    // Unneeded as this just wraps around handleBlockChange()
    // but might as well have it avoid running unnecessary calculations
    @Inject(method = "handleMultiBlockChange", at = @At("HEAD"), cancellable = true)
    public void multiBlockChange(SPacketMultiBlockChange packetMultiBlockChange, CallbackInfo ci) {
        if (cache.isWorldFrozen()) {
            ci.cancel();
        }
    }

    // Never saw that used but might as well
    @Inject(method = "handleExplosion", at = @At("HEAD"), cancellable = true)
    public void explosion(SPacketExplosion packetExplosion, CallbackInfo ci) {
        if (cache.isWorldFrozen()) {
            ci.cancel();
        }
    }


    @Inject(method = "handleUpdateTileEntity", at = @At("RETURN"), cancellable = true)
    public void updateTileEntity(SPacketUpdateTileEntity packetUpdateTileEntity, CallbackInfo ci) {
        if (cache.isWorldFrozen()) {
            ci.cancel();
        }
    }

    @Inject(method = "handleBlockAction", at = @At("RETURN"), cancellable = true)
    public void blockAction(SPacketBlockAction packetBlockAction, CallbackInfo ci) {
        if (cache.isWorldFrozen()) {
            ci.cancel();
        }
    }

    @Inject(method = "handleBlockBreakAnim", at = @At("RETURN"), cancellable = true)
    public void blockBreakAnim(SPacketBlockBreakAnim packetBlockBreakAnim, CallbackInfo ci) {
        if (cache.isWorldFrozen()) {
            ci.cancel();
        }
    }

    private static final int[] blacklistedEntities = {
            76, // EntityFireworkRocket (causes world damage)
            63, // EntityLargeFireball (causes world damage)
            64, // EntitySmallFireball (causes world damage)
            66, // EntityWitherSkull (causes world damage)
            70, // EntityFallingBlock (the one I think was the problem)
            //71, // EntityItemFrame (can count as a block, unneeded when world frozen)
            //77, // EntityLeashKnot (can count as a block, unneeded when world frozen)
    };

    @Inject(method = "handleSpawnObject", at = @At("HEAD"), cancellable = true)
    public void spawnObject(SPacketSpawnObject packetSpawnObject, CallbackInfo ci) {
        if (!cache.isWorldFrozen()) {
            return;
        }

        int thisObjectType = packetSpawnObject.getType();
        if (IntStream.of(blacklistedEntities).anyMatch(type -> type == thisObjectType))
            ci.cancel();
    }


    @Inject(method = "handleUpdateTileEntity", at = @At("HEAD"), cancellable = true)
    public void handleUpdateTileEntity(SPacketUpdateTileEntity packetUpdateTileEntity, CallbackInfo ci) {
        if (cache.isWorldFrozen()) {
            ci.cancel();
        }
    }

    public boolean isChunkUnloaded(int chunkX, int chunkZ) {
        Long2ObjectMap<Chunk> loadedChunks = ChunkProvider.getChunkList();
        if (loadedChunks == null)
            return true;
        

        for (Chunk chunk_ : loadedChunks.values()) {
            if (chunk_.xPosition == chunkX &&
                chunk_.zPosition == chunkZ) {
                return false;
            }
        }
        return true;
    }

    public boolean isChunkLoaded(Chunk chunk) {
        return !isChunkUnloaded(chunk.xPosition, chunk.zPosition);
    }

    public boolean isChunkLoaded(int chunkX, int chunkZ) {
        return !isChunkUnloaded(chunkX, chunkZ);
    }


    // save already loaded chunks
    @Inject(method = "handleChunkData", at = @At("HEAD"), cancellable = true)
    public void chunkData(SPacketChunkData packetChunkData, CallbackInfo ci) {
        if (cache.isWorldFrozen() && isChunkLoaded(packetChunkData.getChunkX(), packetChunkData.getChunkZ())) {
            ci.cancel();
        }
    }

    @Inject(method = "processChunkUnload", at = @At("HEAD"), cancellable = true)
    public void processChunkUnload(SPacketUnloadChunk packetIn, CallbackInfo ci) {
        if (!cache.areChunksUnloadable()) {
            ci.cancel();
        }
    }
}
