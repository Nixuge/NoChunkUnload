package me.nixuge.nochunkunload.mixins.client.network;

import io.netty.buffer.Unpooled;
import me.nixuge.nochunkunload.McMod;
import me.nixuge.nochunkunload.config.Cache;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.*;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

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
    public void blockBreakAnim(S25PacketBlockBreakAnim p_handleBlockBreakAnim_1_, CallbackInfo ci) {
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
    public void spawnObject(S0EPacketSpawnObject p_handleSpawnObject_1_, CallbackInfo ci) {
        if (!cache.isWorldFrozen()) {
            return;
        }

        int thisObjectType = p_handleSpawnObject_1_.getType();
        if (IntStream.of(blacklistedEntities).anyMatch(type -> type == thisObjectType))
            ci.cancel();
    }


    @Inject(method = "handleUpdateTileEntity", at = @At("HEAD"), cancellable = true)
    public void handleUpdateTileEntity(S35PacketUpdateTileEntity p_handleUpdateTileEntity_1_, CallbackInfo ci) {
        if (cache.isWorldFrozen()) {
            ci.cancel();
        }
    }



    @Shadow
    private WorldClient clientWorldController;
    private static final byte[] emptyMinus = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
    private static final byte[] emptyZero = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    private static final ExtendedBlockStorage[] emptyStorage = new ExtendedBlockStorage[] {
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null
    };

    public boolean isChunkUnSavedExtensive(Chunk chunk) {
        return (Arrays.equals(chunk.getBiomeArray(), emptyMinus) ||
                Arrays.equals(chunk.getBiomeArray(), emptyZero) ||
                Arrays.equals(chunk.getBlockStorageArray(), emptyStorage));
    }
    public boolean isChunkUnSavedExtensive(int chunkX, int chunkZ) {
        return isChunkUnSavedExtensive(this.clientWorldController.getChunkFromChunkCoords(chunkX, chunkZ));
    }
    public boolean isChunkSavedExtensive(Chunk chunk) {
        return !isChunkUnSavedExtensive(chunk);
    }
    public boolean isChunkSavedExtensive(int chunkX, int chunkZ) {
        return isChunkSavedExtensive(this.clientWorldController.getChunkFromChunkCoords(chunkX, chunkZ));
    }


    // save already loaded chunks
    @Inject(method = "handleChunkData", at = @At("HEAD"), cancellable = true)
    public void chunkData(S21PacketChunkData p_handleChunkData_1_, CallbackInfo ci) {
        if (cache.isWorldFrozen() && isChunkSavedExtensive(p_handleChunkData_1_.getChunkX(), p_handleChunkData_1_.getChunkZ())) {
            ci.cancel();
        }
    }



    @ModifyVariable(method = "handleMapChunkBulk", at = @At("HEAD"), argsOnly = true)
    public S26PacketMapChunkBulk mapChunkBulk1(S26PacketMapChunkBulk p_handleMapChunkBulk_1_) throws IOException {
        if (!cache.isWorldFrozen()) {
            return p_handleMapChunkBulk_1_;
        }

        // Make a list to store valid chunk indexes
        List<Integer> validIndexes = new ArrayList<>();

        for (int i = 0; i < p_handleMapChunkBulk_1_.getChunkCount(); ++i) {
            if (isChunkUnSavedExtensive( p_handleMapChunkBulk_1_.getChunkX(i), p_handleMapChunkBulk_1_.getChunkZ(i) )) {
                validIndexes.add(i);
            }
        }

        return cache.getPacket(p_handleMapChunkBulk_1_, validIndexes);
//        PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
//        p_handleMapChunkBulk_1_.writePacketData(buf);
//        S26PacketMapChunkBulk newPacket = new S26PacketMapChunkBulk();
//        newPacket.readPacketData(buf);
//        return newPacket;
    }

    @Inject(method = "handleMapChunkBulk", at = @At("HEAD"), cancellable = true)
    public void mapChunkBulk(S26PacketMapChunkBulk p_handleMapChunkBulk_1_, CallbackInfo ci) throws IOException {
        // System.out.println("DAAAMN: " + p_handleMapChunkBulk_1_.getChunkBytes(0)[0]);
        if (Arrays.equals(p_handleMapChunkBulk_1_.getChunkBytes(0), emptyMinus)) {
            System.out.println("EYOOO");
            ci.cancel();
        }
    }
}
