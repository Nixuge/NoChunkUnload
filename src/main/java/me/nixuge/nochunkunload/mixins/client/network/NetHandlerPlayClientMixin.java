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
     *  - (One I'm going with rn) Basically just add a Mixin on all the submethods:
     *  -- doPreChunk (unneeded)
     *  -- invalidateBlockReceiveRegion
     *  -- fillChunk
     *  -- markBlockRangeForRenderUpdate
     *
     * For now, this code is staying here unused.
     * It can be reactivated at any time by just uncommenting the @ModifyVariable line on top of it.
     */

    private int func_180737_a(int p_180737_0_, boolean p_180737_1_, boolean p_180737_2_) {
        int lvt_3_1_ = p_180737_0_ * 2 * 16 * 16 * 16;
        int lvt_4_1_ = p_180737_0_ * 16 * 16 * 16 / 2;
        int lvt_5_1_ = p_180737_1_ ? p_180737_0_ * 16 * 16 * 16 / 2 : 0;
        int lvt_6_1_ = p_180737_2_ ? 256 : 0;
        return lvt_3_1_ + lvt_4_1_ + lvt_5_1_ + lvt_6_1_;
    }




    @ModifyVariable(method = "handleMapChunkBulk", at = @At("HEAD"))
    public S26PacketMapChunkBulk mapChunkBulk(S26PacketMapChunkBulk p_handleMapChunkBulk_1_) throws IOException {
        // Make a list to store valid chunk indexes

        if (!cache.isWorldFrozen()) {
            return p_handleMapChunkBulk_1_;
        }

        List<Integer> validIndexes = new ArrayList<>();
        for (int i = 0; i < p_handleMapChunkBulk_1_.getChunkCount(); ++i) {
            if (!this.cache.isSavedChunk(p_handleMapChunkBulk_1_.getChunkX(i), p_handleMapChunkBulk_1_.getChunkZ(i))) {
                validIndexes.add(i);
            }
        }

        int newSize = validIndexes.size();
        if (newSize == 0)
            return new S26PacketMapChunkBulk();


        System.out.println("Newsize: " + newSize);
        for (int i : validIndexes) {
            //System.out.println("Valid index: " + i);
        }

        // Get data for current packet
        PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
        PacketBuffer buf2 = new PacketBuffer(Unpooled.buffer());
        p_handleMapChunkBulk_1_.writePacketData(buf);
        p_handleMapChunkBulk_1_.writePacketData(buf2);

        PacketBuffer newBuf = new PacketBuffer(Unpooled.buffer());
        //System.out.println("Changed var stage 1!!");

        boolean isOverworld = buf.readBoolean(); //isOverworld
        newBuf.writeBoolean(isOverworld); //isOverworld

        int size = buf.readVarIntFromBuffer(); //size (old read)
        newBuf.writeVarIntToBuffer(size); //size (new write)

        S21PacketChunkData.Extracted[] chunksData = new S21PacketChunkData.Extracted[size];

        //System.out.println("Changed var stage 2!!");
        int newIndex = 0;
        for(int i = 0; i < size; ++i) {
//            if (!validIndexes.contains(i)) {
//                //System.out.println("Skipped iter!!");
//                //skip over the buffer instruction
//                buf.readInt(); buf.readInt();buf.readShort();
//                continue;
//            }
            //System.out.println("Ran iter!!");
            newBuf.writeInt(buf.readInt()); //xPos
            newBuf.writeInt(buf.readInt()); //zPos
            chunksData[newIndex] = new S21PacketChunkData.Extracted();
            short dataSize = buf.readShort();
            newBuf.writeShort(dataSize & '\uffff'); //dataSize
            chunksData[newIndex] = new S21PacketChunkData.Extracted();
            chunksData[newIndex].dataSize = dataSize & '\uffff';
            chunksData[newIndex].data = new byte[this.func_180737_a(Integer.bitCount(chunksData[newIndex].dataSize), isOverworld, true)];

            newIndex++;
        }

        //System.out.println("Changed var stage 3!!");
        newIndex = 0;
        System.out.println("Size: " + size + " NewSize: " + newSize);
        for(int i = 0; i < size; ++i) {
            //System.out.println("iteration rn");
//            if (!validIndexes.contains(i)) {
//                //System.out.println("Skipped!");
//                buf.readBytes(chunksData[i].data); //skip over the buffer instruction
//                continue;
//            }
            byte[] bt = buf.readBytes(chunksData[newIndex].data).array();

            newBuf.writeBytes(bt);
            newIndex++;
        }
        System.out.println("Changed var stage 4!!");

        // instantiate the new packet and give it its needed values
        S26PacketMapChunkBulk newPacket = new S26PacketMapChunkBulk();
        newPacket.readPacketData(buf2);
        System.out.println("Changed var stage FINAL!!");
        System.out.println("new count: " + newPacket.getChunkCount());

        return newPacket;
    }
}
