package me.nixuge.nochunkunload.mixins.client.network;

import io.netty.buffer.Unpooled;
import me.nixuge.nochunkunload.McMod;
import me.nixuge.nochunkunload.config.Cache;
import me.nixuge.nochunkunload.mixins.server.S26PacketMapChunkBulkGetters;
import me.nixuge.nochunkunload.mixins.server.S26PacketMapChunkBulkSetters;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.nio.Buffer;
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
        if (this.cache.isSavedChunk(p_handleChunkData_1_.getChunkX(), p_handleChunkData_1_.getChunkZ())) {
            if (cache.isWorldFrozen()) {
                ci.cancel();
            }
        }
    }
//    @Inject(method = "handleChunkData", at = @At("RETURN"), cancellable = true)
//    public void chunkDataEnd(S21PacketChunkData p_handleChunkData_1_, CallbackInfo ci) {
//        this.cache.addSavedChunk(p_handleChunkData_1_.getChunkX(), p_handleChunkData_1_.getChunkZ());
//    }

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
    /*
     * The function below is supposed to
     */

    private int func_180737_a(int p_180737_0_, boolean p_180737_1_, boolean p_180737_2_) {
        int lvt_3_1_ = p_180737_0_ * 2 * 16 * 16 * 16;
        int lvt_4_1_ = p_180737_0_ * 16 * 16 * 16 / 2;
        int lvt_5_1_ = p_180737_1_ ? p_180737_0_ * 16 * 16 * 16 / 2 : 0;
        int lvt_6_1_ = p_180737_2_ ? 256 : 0;
        return lvt_3_1_ + lvt_4_1_ + lvt_5_1_ + lvt_6_1_;
    }


    private S26PacketMapChunkBulk createNewPacket(
            boolean isOverworld, int bufferSize,
            int[] xPositions, int[] zPositions,
            S21PacketChunkData.Extracted[] chunksData) {

        PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
        buf.writeBoolean(isOverworld);
        buf.writeVarIntToBuffer(chunksData.length);

        int lvt_2_2_;
        for(lvt_2_2_ = 0; lvt_2_2_ < xPositions.length; ++lvt_2_2_) {
            buf.writeInt(xPositions[lvt_2_2_]);
            buf.writeInt(zPositions[lvt_2_2_]);
            buf.writeShort((short)(chunksData[lvt_2_2_].dataSize & '\uffff'));
        }

        for(lvt_2_2_ = 0; lvt_2_2_ < xPositions.length; ++lvt_2_2_) {
            buf.writeBytes(chunksData[lvt_2_2_].data);
        }

        S26PacketMapChunkBulk newPacket = new S26PacketMapChunkBulk();
        try {
            newPacket.readPacketData(buf);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return newPacket;
    }


    // @ModifyVariable(method = "handleMapChunkBulk", at = @At("HEAD"))
    public S26PacketMapChunkBulk mapChunkBulk(S26PacketMapChunkBulk p_handleMapChunkBulk_1_) throws IOException {
        // Make a list to store valid chunk indexes

        if (!cache.isWorldFrozen()) {
            return p_handleMapChunkBulk_1_;
        }

        List<Integer> validIndexes = new ArrayList<>();
        for (int i = 0; i < p_handleMapChunkBulk_1_.getChunkCount(); ++i) {
            if (!this.cache.isSavedChunk2(p_handleMapChunkBulk_1_.getChunkX(i), p_handleMapChunkBulk_1_.getChunkZ(i))) {
                validIndexes.add(i);
            } else {
                this.cache.addSavedChunk2(p_handleMapChunkBulk_1_.getChunkX(i), p_handleMapChunkBulk_1_.getChunkZ(i));
            }
        }

        int newSize = validIndexes.size();
        if (newSize == 0)
            return new S26PacketMapChunkBulk();

        // Get data for current packet
        PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
        p_handleMapChunkBulk_1_.writePacketData(buf);
//

        boolean isOverworld = buf.readBoolean();
        int bufferSize = buf.readVarIntFromBuffer();
        int[] xPositions = new int[newSize];
        int[] zPositions = new int[newSize];
        S21PacketChunkData.Extracted[] chunksData = new S21PacketChunkData.Extracted[newSize];

        int actualI = 0;
        for(int i = 0; i < bufferSize; ++i) {
            if (!validIndexes.contains(i)) {
                buf.readInt(); buf.readInt(); buf.readShort();
                continue;
            }
            xPositions[actualI] = buf.readInt();
            zPositions[actualI] = buf.readInt();
            chunksData[actualI] = new S21PacketChunkData.Extracted();
            chunksData[actualI].dataSize = buf.readShort() & '\uffff';
            chunksData[actualI].data = new byte[this.func_180737_a(Integer.bitCount(chunksData[actualI].dataSize), isOverworld, true)];
            actualI++;
        }


        S26PacketMapChunkBulk pack = createNewPacket(
                isOverworld,
                bufferSize,
                xPositions, zPositions,
                chunksData
        );
        return pack;
    }
}
