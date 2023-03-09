package me.nixuge.nochunkunload.mixins.client.network;

import me.nixuge.nochunkunload.McMod;
import me.nixuge.nochunkunload.config.Cache;
import me.nixuge.nochunkunload.mixins.server.S26PacketMapChunkBulkGetters;
import me.nixuge.nochunkunload.mixins.server.S26PacketMapChunkBulkSetters;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.*;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
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
    private List<int[]> savedChunks = new ArrayList<>();

    private boolean isSaved(int[] newChunk) {
        //System.out.println("Hello isSaved called !");
        for (int[] chunk : savedChunks) {
            if (Arrays.equals(chunk, newChunk))
                return true;
        }
        return false;
    }

    private boolean isSaved(int chunkX, int chunkZ) {
        return isSaved(new int[]{chunkX, chunkZ});
    }

    @Inject(method = "handleChunkData", at = @At("HEAD"), cancellable = true)
    public void chunkData(S21PacketChunkData p_handleChunkData_1_, CallbackInfo ci) {
        // If is a saved chunk
        if (isSaved(p_handleChunkData_1_.getChunkX(), p_handleChunkData_1_.getChunkZ())) {
            if (cache.isWorldFrozen()) {
                ci.cancel();
            }
            return;
        }
        savedChunks.add(new int[]{p_handleChunkData_1_.getChunkX(), p_handleChunkData_1_.getChunkZ()});
    }


    @ModifyVariable(method = "handleMapChunkBulk", at = @At("HEAD"))
    public S26PacketMapChunkBulk mapChunkBulk(S26PacketMapChunkBulk p_handleMapChunkBulk_1_) {
        // Make a list to store valid chunk indexes

        List<Integer> validIndexes = new ArrayList<>();
        for(int i = 0; i < p_handleMapChunkBulk_1_.getChunkCount(); ++i) {
            if (!isSaved(p_handleMapChunkBulk_1_.getChunkX(i), p_handleMapChunkBulk_1_.getChunkZ(i))) {
                validIndexes.add(i);
            }
        }

        int newSize = validIndexes.size();
        if (newSize == 0) {
//            World world = Minecraft.getMinecraft().thePlayer.worldObj;
//            Chunk owo = new Chunk(world, 0, 0);
//            List<Chunk> arr = new ArrayList<>();
//            arr.add(owo);
            return new S26PacketMapChunkBulk();
        }

        //return p_handleMapChunkBulk_1_;
//
        // Get data for current packet
        boolean isOverworld = ((S26PacketMapChunkBulkGetters) p_handleMapChunkBulk_1_).isOverworld();
        S21PacketChunkData.Extracted[] oldChunkData = ((S26PacketMapChunkBulkGetters) p_handleMapChunkBulk_1_).getChunksData();
        int[] oldxPositions = ((S26PacketMapChunkBulkGetters) p_handleMapChunkBulk_1_).getxPositions();
        int[] oldzPositions = ((S26PacketMapChunkBulkGetters) p_handleMapChunkBulk_1_).getzPositions();

            S21PacketChunkData.Extracted[] truc = new Owo(p_handleMapChunkBulk_1_).getOwo();
//        // Make empty data for the new packet
//        S21PacketChunkData.Extracted[] chunkData = new S21PacketChunkData.Extracted[newSize];
//        int[] xPositions = new int[newSize];
//        int[] zPositions = new int[newSize];
//
//        //
//        int newIndex = 0;
//        for (int index : validIndexes) {
//            chunkData[newIndex] = oldChunkData[index];
//            xPositions[newIndex] = oldxPositions[index];
//            zPositions[newIndex] = oldzPositions[index];
//            newIndex++;
//        }

        return p_handleMapChunkBulk_1_;

        // instantiate the new packet and give it its needed values
//        S26PacketMapChunkBulk newPacket = new S26PacketMapChunkBulk();
//
//        S26PacketMapChunkBulkSetters setter = ((S26PacketMapChunkBulkSetters) newPacket);
//        setter.setChunks(chunkData);
//        setter.setxPositions(xPositions);
//        setter.setzPositions(zPositions);
//        setter.setIsOverworld(isOverworld);
//
//
//        return newPacket;
    }


        //SPLIT HERE
        // NOTE:
        // this isn't perfect, as if
        // both already saved & unsaved chunks are in there
        // it won't save new chunks
        // however, marking it as complete
//        for(int i = 0; i < p_handleMapChunkBulk_1_.getChunkCount(); ++i) {
//            p_handleMapChunkBulk_1_.get
//
//        }
//
//
//        if (cache.isWorldFrozen()) {
//            for(int i = 0; i < p_handleMapChunkBulk_1_.getChunkCount(); ++i) {
//                System.out.println("ALREADY SAVED TRY! " + p_handleMapChunkBulk_1_.getChunkX(i) + " " + p_handleMapChunkBulk_1_.getChunkZ(i));
//                if (isSaved(p_handleMapChunkBulk_1_.getChunkX(i), p_handleMapChunkBulk_1_.getChunkZ(i))) {
//                    System.out.println("CONTAINS ALREADY SAVED CHUNK!!!!");
//                    //ci.cancel();
//                }
//            }
//        }

}
