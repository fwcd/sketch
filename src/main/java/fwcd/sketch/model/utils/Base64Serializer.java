package fwcd.sketch.model.utils;

import java.lang.reflect.Type;
import java.util.Base64;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * A type adapter that encodes and decodes byte arrays as
 * Base64 JSON strings.
 */
public class Base64Serializer implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {
	@Override
	public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(Base64.getEncoder().encodeToString(src));
	}
	
	@Override
	public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
		return Base64.getDecoder().decode(json.getAsString());
	}
}
