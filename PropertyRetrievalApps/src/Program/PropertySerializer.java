package Program;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class PropertySerializer implements JsonSerializer<Property>{

	@Override
	public JsonElement serialize(Property property, Type type, JsonSerializationContext context) {
		JsonObject result = new JsonObject();
		result.add("Id", new JsonPrimitive(property.getId()));
		result.add("Address", new JsonPrimitive(property.getAddress()));
		result.add("Price", new JsonPrimitive(property.getPrice()));
		String ImageString = "";
		for(String image:property.getImages()){
			ImageString = ImageString.concat(image + ",");
		}
		result.add("Images", new JsonPrimitive(ImageString));
		//result.add("Images", new JsonPrimitive(property.getImages()));
		Map<String,String> map = property.getFeatures();
		JsonObject featureMap = new JsonObject();
		for(Map.Entry<String, String> entry:map.entrySet()){
			featureMap.add(entry.getKey(), new JsonPrimitive(entry.getValue()));
		}
		result.add("Features", featureMap);
		return result;
	}

}
