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
		// Minecraft minecraft = 
		WorldClient worldClient = Minecraft.getMinecraft().theWorld;
		// System.out.println("===== DATA: " + worldClient.getClass().toString());

		Class<?> typeOfObject = ChunkProviderClient.class;
		Class<?> typeOfField = List.class;
		List<Chunk> chunkList = new ArrayList<>();

		try {
			Field f = ReflectionUtils.findField(typeOfObject, typeOfField);
			chunkList = (List<Chunk>) typeOfField.cast(f.get(worldClient.getChunkProvider()));
			System.out.println("got chunkList");
		} catch (Exception e) {
			System.out.println("NASTY EXCEPTION");
		}
		
		// Notes:
		// 1

		return chunkList;
	}
}
