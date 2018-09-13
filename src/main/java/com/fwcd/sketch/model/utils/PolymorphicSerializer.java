package com.fwcd.sketch.model.utils;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class PolymorphicSerializer<T> implements JsonSerializer<T>, JsonDeserializer<T> {
	@Override
	public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject serialized = new JsonObject();
		
		serialized.add("INSTANCE", context.serialize(src));
		serialized.addProperty("CLASSNAME", src.getClass().getName());
		
		return serialized;
	}
	
	@Override
	public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject serialized = json.getAsJsonObject();
		
		String className = serialized.get("CLASSNAME").getAsString();
		JsonElement instance = serialized.get("INSTANCE");
		
		try {
			return context.deserialize(instance, Class.forName(className));
		} catch (ClassNotFoundException e) {
			throw new JsonParseException(e);
		}
	}
}
