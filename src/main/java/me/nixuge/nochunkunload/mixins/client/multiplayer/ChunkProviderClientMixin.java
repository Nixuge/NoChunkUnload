package me.nixuge.nochunkunload.mixins.client.multiplayer;

import me.nixuge.nochunkunload.McMod;
import me.nixuge.nochunkunload.config.Cache;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.LongHashMap;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkProviderClient.class)
public class ChunkProviderClientMixin {
    private final Cache cache = McMod.getInstance().getCache();

    @Inject(method="unloadChunk", at = @At("HEAD"), cancellable = true)
    public void unloadChunk(int int1, int int2, CallbackInfo ci) {
        if (!cache.areChunksUnloadable()) {
            ci.cancel();
        }
    }

    @Inject(method="unloadQueuedChunks", at = @At("HEAD"), cancellable = true)
    public void unloadChunk(CallbackInfoReturnable<Boolean> cir) {
        if (!cache.areChunksUnloadable()) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Shadow
    private LongHashMap<Chunk> chunkMapping;
    @Shadow
    private List<Chunk> chunkListing;

    @Inject(method="loadChunk", at = { @At("RETURN") }, cancellable = true)
    public void loadChunk(int chunkX, int chunkZ, CallbackInfoReturnable<Chunk> cir) {
        System.out.println("LOADING CLIENT CHUNK");
        Chunk chunk = null;
        WorldClient worldClient = Minecraft.getMinecraft().theWorld;
        try {
            // packet receive part
            worldClient.invalidateBlockReceiveRegion(chunkX << 4, 0, chunkZ << 4, (chunkX << 4) + 15, 256, (chunkZ << 4) + 15);

            //added part
            AnvilChunkLoader loader = new AnvilChunkLoader(new File("saves/owo"));
            chunk = loader.loadChunk(Minecraft.getMinecraft().theWorld, chunkX, chunkZ);

            Chunk chunk2 = this.chunkMapping.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ));
            
            chunk2.setStorageArrays(chunk.getBlockStorageArray());
            chunk2.setBiomeArray(chunk.getBiomeArray());
            chunk2.setHeightMap(chunk.getHeightMap());
            chunk2.setHasEntities(chunk.getEntityLists().length > 0);

    //         // loadChunk part
    //         System.out.println(chunk);
    //         chunkMapping.add(ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ), chunk);
    //         chunkListing.add(chunk);
    //         net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.world.ChunkEvent.Load(chunk));
    //         chunk.setChunkLoaded(true);  

    //         // packet receive part again
    //         // chunk.fillChunk(packetIn.getExtractedDataBytes(), packetIn.getExtractedSize(), packetIn.func_149274_i());
    //         worldClient.markBlockRangeForRenderUpdate(chunkX << 4, 0, chunkZ << 4, (chunkX << 4) + 15, 256, (chunkZ << 4) + 15);


        } catch (IOException e) {
            e.printStackTrace();
        }
    //     if (chunk != null) {
    //         System.out.println(chunk.getWorld() + " vs " + Minecraft.getMinecraft().theWorld);
    //         cir.setReturnValue(chunk);
    //     }



        // AnvilChunkLoader anvilChunkLoader = new AnvilChunkLoader(new File("saves/owo"));
        // // chunkLoader.loadChunk(null, getRequiredPermissionLevel(), getRequiredPermissionLevel())
        // // chunkLoader.sa
        // WorldClient worldClient = Minecraft.getMinecraft().theWorld;
        // // // worldClient.loadD
        // if (!(
        //     (chunkX == 0 && chunkZ == 0) ||
        //     (chunkX == 1 && chunkZ == 1) ||
        //     (chunkX == 0 && chunkZ == 1) ||
        //     (chunkX == 1 && chunkZ == 0)
        //     )) {
        //     return;
        // }

        // CURRENT STATUS:
        // As of now, the chunk terrain itself seems to come from the first argument of
        // anvilChunkLoader.loadChunk (which is a worldclient, which makes it like an infinite loop?)
        // TODO: try and find a way to make that world from local files.
        
        // Use intellij's ctrl+f to try and find things


        // try {
        //     Chunk chunk = anvilChunkLoader.loadChunk(worldClient, 0, 0);
        //     //Chunk chunk = anvilChunkLoader.loadChunk(null, 0, 0);

        //     chunkMapping.add(ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ), chunk);
        //     chunkListing.add(chunk);
        //     net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.world.ChunkEvent.Load(chunk));
        //     chunk.setChunkLoaded(true);
        //     System.out.println("SUccessfully loaded chunk from here.");
        //     cir.setReturnValue(chunk);
        //     cir.cancel();
        //     return;
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
    }
}
