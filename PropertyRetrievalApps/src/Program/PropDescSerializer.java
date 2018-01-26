package Program;
import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class PropDescSerializer implements JsonSerializer<PropertyDescription> {
	
	@Override
	public JsonElement serialize(PropertyDescription propertyDesc, Type type, JsonSerializationContext context) {
		JsonObject result = new JsonObject();
		result.add("Id", new JsonPrimitive(propertyDesc.getId()));
		result.add("Title", new JsonPrimitive(propertyDesc.getTitle()));
		result.add("Body", new JsonPrimitive(propertyDesc.getBody()));
		return result;
		
	}
}
