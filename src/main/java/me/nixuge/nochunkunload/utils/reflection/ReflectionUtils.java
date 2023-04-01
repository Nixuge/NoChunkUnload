package me.nixuge.nochunkunload.utils.reflection;

import java.lang.reflect.Field;

public class ReflectionUtils {
	public static Field findField(Class<?> typeOfClass, Class<?> typeOfField) {
		Field[] fields = typeOfClass.getDeclaredFields();

		for (Field f : fields) {
			if (f.getType().equals(typeOfField)) {
				try {
					f.setAccessible(true);
					return f;
				} catch (Exception e) {
					throw new RuntimeException(
							"Couldn't get private Field of type \"" + typeOfField +
							"\" from class \"" + typeOfClass + "\" !", e);
				}
			}
		}

		throw new RuntimeException(
				"Couldn't find any Field of type \"" + typeOfField + 
				"\" from class \"" + typeOfClass + "\" !");
	}
}
