package me.nixuge.nochunkunload.utils.reflection;

import java.lang.reflect.Field;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.world.chunk.Chunk;

public class ChunkProvider {
    @SuppressWarnings("unchecked")
    public static Long2ObjectMap<Chunk> getChunkList() {
        /* 
         * Notes:
         * 1- For some reason, just calling anything on worldClient.getChunkProvider
         *    (may it be saving it in a variable or calling a function on it)
         *    produces a CastError
         *    (java.lang.ClassCastException: Cannot cast net.minecraft.client.network.NetHandlerLoginClient to net.minecraft.client.network.NetHandlerPlayClient)
         *    This only happens 1x it seems as the player logins, but that's the reason of that weird try catch.
         * 
         * 2- This could prolly be optimized to cache the worldChunk, but honestly this mod already hits hard
         *    on performances in other ways (60 render distance) so might as well just get that list
         *    every time, in case mc changes its chunk list instance or something.
         * 
         * 3- Why use reflections to get the chunkList instead of just using worldClient.getChunkProvider().chunkExists(chunkX, chunkZ) ?
         *    Because unlike what this guy says https://forums.minecraftforge.net/topic/26474-1710check-if-chunk-is-loaded/?do=findComment&comment=136879
         *    for some reason that always returns true (at least from my tests, both in dev & prod)
         *    & so is unusable
         */
		
        WorldClient worldClient = Minecraft.getMinecraft().theWorld;

        Class<?> typeOfObject = ChunkProviderClient.class;
        Class<?> typeOfField = Long2ObjectMap.class;
        Long2ObjectMap<Chunk> chunkList = null;

        try {
            Field f = ReflectionUtils.findField(typeOfObject, typeOfField); // Cached anyways
            chunkList = (Long2ObjectMap<Chunk>) typeOfField.cast(f.get(worldClient.getChunkProvider()));
        } catch (Exception e) {
            System.out.println("Exception happened getting chunk list. This is normal when the world first loads.");
            // System.out.println("This is fine if it happens 1x as that's how it seems to be with Forge.");
            // System.out.println("However, is this is spammed in your console, there's a big issue");
            // System.out.println("in which case you should report the issue on github.");
        }
        
		return chunkList;
    }
}
