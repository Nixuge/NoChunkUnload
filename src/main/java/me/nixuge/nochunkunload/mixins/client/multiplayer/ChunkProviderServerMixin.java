package me.nixuge.nochunkunload.mixins.client.multiplayer;

import java.io.File;
import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.gen.ChunkProviderServer;

@Mixin(ChunkProviderServer.class)
public class ChunkProviderServerMixin {
    @Inject(method = "loadChunk", at = @At("HEAD"), cancellable = true)
    public void loadChunk(int x, int z, CallbackInfoReturnable<Chunk> ci) {
        System.out.println("LOADING SERVER CHUNK");
        Chunk chunk = null;
        try {
            AnvilChunkLoader loader = new AnvilChunkLoader(new File("saves/owo"));
            chunk = loader.loadChunk(Minecraft.getMinecraft().theWorld, x, z);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (chunk != null) {
            ci.setReturnValue(chunk);
        }
    }

}
