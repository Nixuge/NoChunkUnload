package me.nixuge.nochunkunload.utils.reflection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.world.chunk.Chunk;

public class ChunkProvider {
    // private field
    public ChunkProvider() {

    }

	@SuppressWarnings("unchecked")
	public static List<Chunk> getChunkList() {
		WorldClient worldClient = Minecraft.getMinecraft().theWorld;

		Class<?> typeOfObject = ChunkProviderClient.class;
		Class<?> typeOfField = List.class;
		List<Chunk> chunkList = new ArrayList<>();

		try {
			Field f = ReflectionUtils.findField(typeOfObject, typeOfField);
			chunkList = (List<Chunk>) typeOfField.cast(f.get(worldClient.getChunkProvider()));
		} catch (Exception e) {
			System.out.println("Exception happened getting chunk list !");
			System.out.println("This is fine if it happens 1x as that's how it seems to be with Forge.");
			System.out.println("However, is this is spammed in your console, there's a big issue");
			System.out.println("in which case you should report the issue on github.");
		}
		
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
		 */

		return chunkList;
	}
}
