package me.nixuge.nochunkunload.mixins.client.network;

import me.nixuge.nochunkunload.McMod;
import me.nixuge.nochunkunload.config.Cache;
import me.nixuge.nochunkunload.utils.packet.PacketUtils;
import me.nixuge.nochunkunload.utils.reflection.ChunkProvider;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.*;
import net.minecraft.world.chunk.Chunk;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Mixin(NetHandlerPlayClient.class)
public class NetHandlerPlayClientMixin {
    private final Cache cache = McMod.getInstance().getCache();
    private final PacketUtils packetUtils = McMod.getInstance().getPacketUtils();

    @Inject(method = "handleBlockChange", at = @At("HEAD"), cancellable = true)
    public void blockChange(S23PacketBlockChange packetBlockChange, CallbackInfo ci) {
        if (cache.isWorldFrozen()) {
            ci.cancel();
        }
    }

    // Unneeded as this just wraps around handleBlockChange()
    // but might as well have it avoid running unnecessary calculations
    @Inject(method = "handleMultiBlockChange", at = @At("HEAD"), cancellable = true)
    public void multiBlockChange(S22PacketMultiBlockChange packetMultiBlockChange, CallbackInfo ci) {
        if (cache.isWorldFrozen()) {
            ci.cancel();
        }
    }

    // Never saw that used but might as well
    @Inject(method = "handleExplosion", at = @At("HEAD"), cancellable = true)
    public void explosion(S27PacketExplosion packetExplosion, CallbackInfo ci) {
        if (cache.isWorldFrozen()) {
            ci.cancel();
        }
    }


    @Inject(method = "handleUpdateTileEntity", at = @At("RETURN"), cancellable = true)
    public void updateTileEntity(S35PacketUpdateTileEntity packetUpdateTileEntity, CallbackInfo ci) {
        if (cache.isWorldFrozen()) {
            ci.cancel();
        }
    }

    @Inject(method = "handleBlockAction", at = @At("RETURN"), cancellable = true)
    public void blockAction(S24PacketBlockAction packetBlockAction, CallbackInfo ci) {
        if (cache.isWorldFrozen()) {
            ci.cancel();
        }
    }

    @Inject(method = "handleBlockBreakAnim", at = @At("RETURN"), cancellable = true)
    public void blockBreakAnim(S25PacketBlockBreakAnim packetBlockBreakAnim, CallbackInfo ci) {
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
    public void spawnObject(S0EPacketSpawnObject packetSpawnObject, CallbackInfo ci) {
        if (!cache.isWorldFrozen()) {
            return;
        }

        int thisObjectType = packetSpawnObject.getType();
        if (IntStream.of(blacklistedEntities).anyMatch(type -> type == thisObjectType))
            ci.cancel();
    }


    @Inject(method = "handleUpdateTileEntity", at = @At("HEAD"), cancellable = true)
    public void handleUpdateTileEntity(S35PacketUpdateTileEntity packetUpdateTileEntity, CallbackInfo ci) {
        if (cache.isWorldFrozen()) {
            ci.cancel();
        }
    }

    public boolean isChunkUnloaded(int chunkX, int chunkZ) {
        List<Chunk> loadedChunks = ChunkProvider.getChunkList();

        for (Chunk chunk_ : loadedChunks) {
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
    public void chunkData(S21PacketChunkData packetChunkData, CallbackInfo ci) {
        ci.cancel();
        if (cache.isWorldFrozen() && isChunkLoaded(packetChunkData.getChunkX(), packetChunkData.getChunkZ())) {
            ci.cancel();
        }
    }

    @ModifyVariable(method = "handleMapChunkBulk", at = @At("HEAD"), argsOnly = true)
    public S26PacketMapChunkBulk mapChunkBulk(S26PacketMapChunkBulk packetMapChunkBulk) {
        if (!cache.isWorldFrozen()) {
            return packetMapChunkBulk;
        }

        List<Integer> validIndexes = new ArrayList<>();

        for (int i = 0; i < packetMapChunkBulk.getChunkCount(); ++i) {
            if (isChunkUnloaded( packetMapChunkBulk.getChunkX(i), packetMapChunkBulk.getChunkZ(i) )) {
                validIndexes.add(i);
            }
        }

        return packetUtils.getPacket(packetMapChunkBulk, validIndexes);
    }


    private static final byte[] emptyMinus = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };

    /**
     * See S26PacketMaker's setValuesToEmptyPacket()
     * Basically cancels packets with an array of 256 "-1" bytes as their chunk bytes,
     * which are set by myself in the @ModifyVariable(method = "handleMapChunkBulk") if
     * all the chunks in the inbound packet are already saved.
     */
    @Inject(method = "handleMapChunkBulk", at = @At("HEAD"), cancellable = true)
    public void mapChunkBulk(S26PacketMapChunkBulk packetMapChunkBulk, CallbackInfo ci) {
        if (Arrays.equals(packetMapChunkBulk.getChunkBytes(0), emptyMinus)) {
            ci.cancel();
        }
    }
}
